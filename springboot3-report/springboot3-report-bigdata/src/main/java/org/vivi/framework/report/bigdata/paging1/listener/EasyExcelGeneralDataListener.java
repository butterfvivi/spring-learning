package org.vivi.framework.report.bigdata.paging1.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class EasyExcelGeneralDataListener<T> extends AnalysisEventListener<T> {


    /**
     * 用于存储读取的数据保存数据到DB
     */
    private final List<T> dataList;

    /**
     * 读取集合长度list长度
     */
    private final int pageSize;
    /**
     * 用来存入数据
     */
    private final Consumer<List<T>> consumer;

    public EasyExcelGeneralDataListener(int pageSize, Consumer<List<T>> consumer) {
        this.pageSize = pageSize;
        this.consumer = consumer;
        dataList = new ArrayList<>(pageSize);
    }


    /**
     * onException：当在 Excel 解析过程中发生异常时被触发。
     * invoke：每解析一条数据时被触发，可以在该方法中进行自定义的业务处理。
     * extra：当读取到额外信息（如合并单元格、批注等）时被触发。
     * doAfterAllAnalysed：所有数据解析完成后被触发，可以在该方法中执行一些后续操作。
     * hasNext：判断是否还有下一个对象需要解析。
     * <p>
     * <p>
     * EasyExcel：入口类，用于构建开始各种操作；
     * ExcelReaderBuilder：构建出一个ReadWorkbook对象，即一个工作簿对象，对应的是一个Excel文件；
     * ExcelWriterBuilder：构建出一个WriteWorkbook对象，即一个工作簿对象，对应的是一个Excel文件；
     * ExcelReaderSheetBuilder：构建出一个ReadSheet对象，即一个工作表的对象，对应的Excel中的每个sheet，一个工作簿可以有多个工作表；
     * ExcelWriterSheetBuilder：构建出一WriteSheet对象，即一个工作表的对象，对应的Excel中的每个sheet，一个工作簿可以有多个工作表；
     * ReadListener：在每一行读取完毕后都会调用ReadListener来处理数据，我们可以把调用service的代码可以写在其invoke方法内部；
     * WriteHandler：在每一个操作包括创建单元格、创建表格等都会调用WriteHandler来处理数据，对使用者透明不可见；
     */

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {

        // 处理每一行数据
        log.info("读取的每一条数据：{}", t);
        //数据add进入集合
        dataList.add(t);
        if (dataList.size() >= pageSize) {
            consumer.accept(dataList);
            dataList.clear();
        }

    }

    /**
     * Excel中所有数据解析完毕会调用此方法
     *
     * @param: context
     * @MethodName: doAfterAllAnalysed
     * @return: void
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据完事：{}", JSONUtil.toJsonStr(context));
        consumer.accept(dataList);
        dataList.clear();
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        // 处理异常
        log.info("解析 Excel 文件出错：{}", exception.getMessage());

        super.onException(exception, context);
    }


    @Override
    public void extra(CellExtra extra, AnalysisContext context) {

        // 处理额外信息
        log.info("读取到额外信息extra：{},content:{}", JSONUtil.toJsonStr(extra)

                , JSONUtil.toJsonStr(context)
        );
        super.extra(extra, context);
    }

    @Override
    public boolean hasNext(AnalysisContext context) {

        return super.hasNext(context);
    }


}
