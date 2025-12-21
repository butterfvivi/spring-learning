package org.vivi.framework.canal.conf;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

@Component
public class CanalClient implements InitializingBean {

//    @Value("${canal-monitor-mysql.host}")
//    String canalMonitorHost;
//
//    @Value("${canal-monitor-mysql.port}")
//    Integer canalMonitorPort;

    private final static int BATCH_SIZE = 10000;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("14.103.123.142", 11111),
                "test",
                "canal",
                "canal");
        try {
            // 打开连接
            connector.connect();
            // 订阅数据库表，全部表
            connector.subscribe(".*\\..*");
            // 回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
            connector.rollback();

            while (true) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(100);
                // 获取批量ID
                long batchId = message.getId();
                // 获取数据
                List<Entry> entries = message.getEntries();
                //如果没有数据,休息1秒
                if (batchId == -1 || entries.isEmpty()) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    //如果有数据,处理数据
                    printEntry(message.getEntries());
                }
                // 提交确认
                connector.ack(batchId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            connector.disconnect();
        }
    }

    private static void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                // 开启/关闭事务的实体消息，跳过
                continue;
            }

            //RowChange对象，包含了一行数据变化的所有特征
            //比如isDdl 是否是ddl变更操作 sql 具体的ddl sql beforeColumns afterColumns 变更前后的数据字段等等
            CanalEntry.RowChange rowChage;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }

            // 获取操作类型， insert/update/delete/ddl
            CanalEntry.EventType eventType = rowChage.getEventType();
            // 打印Header信息
            System.out.println(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(), entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            //判断是否是DDL语句
            if (rowChage.getIsDdl()) {
                System.out.println("================&gt; SQL: " + rowChage.getSql());
            }

            //获取RowChange对象里的每一行数据，打印出来
            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                //如果是删除语句,打印删除前的数据
                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                    // 如果是新增语句,打印新增后的数据
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else if (eventType == CanalEntry.EventType.UPDATE) {
                    //变更前的数据
                    System.out.println("------->; before");
                    printColumn(rowData.getBeforeColumnsList());
                    //变更后的数据
                    System.out.println("------->; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

}
