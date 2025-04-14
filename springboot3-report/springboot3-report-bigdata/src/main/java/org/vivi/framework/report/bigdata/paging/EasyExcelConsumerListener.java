package org.vivi.framework.report.bigdata.paging;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * EasyExcel消费监听
 */
public class EasyExcelConsumerListener<T> extends AnalysisEventListener<T> {
    private final int PAGE_SIZE;
    private final List<T> LIST;
    private final Consumer<List<T>> CONSUMER;

    public EasyExcelConsumerListener(int pageSize, Consumer<List<T>> consumer) {
        this.PAGE_SIZE = pageSize;
        this.CONSUMER = consumer;
        LIST = new ArrayList<>(pageSize);
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        LIST.add(data);
        if (LIST.size() >= PAGE_SIZE) {
            CONSUMER.accept(LIST);
            LIST.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        CONSUMER.accept(LIST);
    }
}
