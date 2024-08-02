package org.vivi.framework.iexcelsimple.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public class CustomReadListener<T> extends AnalysisEventListener<T> {

    /**
     * 分批数量
     */
    private final int batchSize;

    /**
     * 分批缓存的集合
     */
    private List<T> cacheData;

    /**
     * 消费函数每次回调批量保存的方法
     */
    private final Consumer<List<T>> consumer;

    /**
     * 可以理解为过滤器 过滤指定的属性值不去读取
     */
    private Predicate<T> predicate;

    /**
     * 单元格解析异常
     */
    @Getter
    private List<ExcelDataConvertException> excelDataConvertExceptionList = new ArrayList<>();


    public CustomReadListener(Consumer<List<T>> consumer) {
        this(1000, consumer);
    }

    public CustomReadListener(Predicate<T> predicate, Consumer<List<T>> consumer) {
        this(1000, predicate, consumer);
    }

    public CustomReadListener(int batchSize, Consumer<List<T>> consumer) {
        this.batchSize = batchSize;
        this.consumer = consumer;
        this.cacheData = new ArrayList<>(this.batchSize);
    }

    public CustomReadListener(int batchSize, Predicate<T> predicate, Consumer<List<T>> consumer) {
        this.batchSize = batchSize;
        this.predicate = predicate;
        this.consumer = consumer;
        this.cacheData = new ArrayList<>(this.batchSize);
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        //log.info("【invoke 方法】==> data: {} context:{} ", data, context);
        if (predicate != null && !this.predicate.test(data)) {
            return;
        }
        this.cacheData.add(data);
        if (this.cacheData.size() >= this.batchSize) {
            this.consumer.accept(this.cacheData);
            this.cacheData.clear();
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //log.info("【doAfterAllAnalysed 方法】==> context:{} ", context);
        //最终执行完成时满足批量的条数剩余的行数，补偿
        if (this.cacheData.size() > 0) {
            this.consumer.accept(this.cacheData);
            this.cacheData.clear();
        }
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.warn("解析异常:{}", exception);
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            Integer rowIndex = excelDataConvertException.getRowIndex();
            Integer columnIndex = excelDataConvertException.getColumnIndex();
            String cellData = excelDataConvertException.getCellData().getStringValue();
            String message = excelDataConvertException.getMessage();
            log.warn("第{}行，第{}列，单元格值[{}]，解析异常:{}", rowIndex, columnIndex, cellData, message);
            this.excelDataConvertExceptionList.add(excelDataConvertException);
        } else {
            throw new ExcelAnalysisException("excel解析异常  {}" + exception.getMessage());
        }
    }

}
