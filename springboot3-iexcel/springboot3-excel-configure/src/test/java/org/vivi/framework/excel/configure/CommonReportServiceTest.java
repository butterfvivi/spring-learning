package org.vivi.framework.excel.configure;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;
import org.vivi.framework.excel.configure.core.service.CommonExportService;
import org.vivi.framework.excel.configure.core.service.common.WriteExcelService;
import org.vivi.framework.excel.configure.mybatis.mapper.ISqlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-dao.xml")
public class CommonReportServiceTest {

    @Autowired
    private ISqlMapper sqlMapper;


    @Autowired
    private CommonExportService commonExportService;

    @Autowired
    private WriteExcelService writeExcelService;

    @Test
    public void testGetCount() {

        List<String> sqlList = new ArrayList<>(1000000);


        for (int i = 500000; i < 700000; i++) {
            StringBuilder builder = new StringBuilder("insert  into `account`(`id`,`account_number`,`password`,`money`,`client_id`) ");
            String sql = "values (" + i + ",'" + i + "iui3u23inskadnfkasdf" + i + "','" + i + "','" + i + "'," + i + ");";
            builder.append(sql);
            sqlList.add(builder.toString());
        }

        File file = new File("E:\\tmp\\batchSql.sql");

        try {
            FileUtils.writeLines(file, sqlList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetMap() {
        List<Map<String, Object>> mapList = sqlMapper.selectList("select * from account");
        System.out.println("mapList = " + JSON.toJSONString(mapList));
    }

    @Test
    public void testExport() {

        long startTime = System.currentTimeMillis();

        ExportBeanConfig exportBeanConfigDto = new ExportBeanConfig();
        String countSql = "select count(*) from account where id  > 50000";

        // String querySql = "SELECT id,account_number AS accountNumber,PASSWORD,client_id AS clientId FROM account";
        String querySql = "SELECT id,account_number AS accountNumber,PASSWORD,client_id AS clientId FROM account " +
                " where id >= (SELECT id FROM account   LIMIT $LIMIT_CYCLE_COUNT, 1 ) and id > 50000 LIMIT $LIMIT_COUNT";
        exportBeanConfigDto.setCountSql(countSql);
        exportBeanConfigDto.setQuerySql(querySql);
        exportBeanConfigDto.setLimitCount(30000);
        exportBeanConfigDto.setSize(30000);
        exportBeanConfigDto.setUseObjectModel(false);
        exportBeanConfigDto.setUseParallelQuery(true);
        //exportBeanConfigDto.setT(new ArrayList<>());
        //exportBeanConfigDto.setT(new AccountEntity());
        try {
            List list = commonExportService.getExportData(exportBeanConfigDto);
            System.out.println(list.size() + "================");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
