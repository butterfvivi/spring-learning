package org.vivi.framework.report.bigdata.paging;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.vivi.framework.report.bigdata.paging.limiter.SlidingWindow;
import org.vivi.framework.report.bigdata.paging.paralle.ParallelUtil;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.LongFunction;

/**
 * Excel工具类
 */
public class ExcelUtil extends EasyExcel {
    // excel sheet最大行数(算标题)
    public static final Integer EXCEL_SHEET_ROW_MAX_SIZE = 1000001;
    private static final int DEF_PARALLEL_NUM = Math.min(Runtime.getRuntime().availableProcessors(), 4);
    private static final int DEF_PAGE_SIZE = 1000;

    private static final String SHEET1="sheet1";
    private static final String SHEET="sheet";


    private ExcelUtil() {}

    public static <T> ExcelReaderBuilder read(String pathName, Class<T> head, Consumer<List<T>> consumer) {
        return read(pathName, head, DEF_PAGE_SIZE, consumer);
    }

    public static <T> ExcelReaderBuilder read(File file, Class<T> head, Consumer<List<T>> consumer) {
        return read(file, head, DEF_PAGE_SIZE, consumer);
    }

    public static <T> ExcelReaderBuilder read(InputStream inputStream, Class<T> head, Consumer<List<T>> consumer) {
        return read(inputStream, head, new EasyExcelConsumerListener<>(DEF_PAGE_SIZE, consumer));
    }

    public static <T> ExcelReaderBuilder read(String pathName, Class<T> head, Integer pageSize, Consumer<List<T>> consumer) {
        return read(pathName, head, new EasyExcelConsumerListener<>(pageSize, consumer));
    }

    public static <T> ExcelReaderBuilder read(File file, Class<T> head, Integer pageSize, Consumer<List<T>> consumer) {
        return read(file, head, new EasyExcelConsumerListener<>(pageSize, consumer));
    }

    public static <T> ExcelReaderBuilder read(InputStream inputStream, Class<T> head, Integer pageSize, Consumer<List<T>> consumer) {
        return read(inputStream, head, new EasyExcelConsumerListener<>(pageSize, consumer));
    }

    public static <T> void write(String pathName, Class<T> head, long totalPage, LongFunction<List<T>> pageListFunction) {
        pageExcelWriter(EasyExcelFactory.write(pathName, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), totalPage, pageListFunction);
    }

    public static <T> void write(File file, Class<T> head, long totalPage, LongFunction<List<T>> pageListFunction) {
        pageExcelWriter(EasyExcelFactory.write(file, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), totalPage, pageListFunction);
    }

    public static <T> void write(OutputStream outputStream, Class<T> head, long totalPage, LongFunction<List<T>> pageListFunction) {
        pageExcelWriter(EasyExcelFactory.write(outputStream, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), totalPage, pageListFunction);
    }

    private static <T> void pageExcelWriter(ExcelWriter excelWriter, long totalPage, LongFunction<List<T>> pageListFunction) {
        try (excelWriter) {
            // 如果无待写入的数据则写入标题
            if (totalPage <= 0) {
                excelWriter.write(Collections.emptyList(), EasyExcelFactory.writerSheet().build());
                return;
            }
            AtomicLong count = new AtomicLong();
            WriteSheet writeSheet = EasyExcelFactory.writerSheet(1, SHEET1).build();
            for (int pageNo = 1; pageNo <= totalPage; pageNo++) {
                List<T> pageList = pageListFunction.apply(pageNo);
                writeSheet.setSheetNo((int) (count.addAndGet(pageList.size()) / EXCEL_SHEET_ROW_MAX_SIZE + 1));
                writeSheet.setSheetName(SHEET + writeSheet.getSheetNo());
                excelWriter.write(pageList, writeSheet); // excel写入数据
            }
        }
    }

    public static <T> void writeForParallel(String pathName, Class<T> head, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException {
        pageExcelWriterParallel(EasyExcelFactory.write(pathName, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), DEF_PARALLEL_NUM, totalPage, pageListFunction);
    }

    public static <T> void writeForParallel(File file, Class<T> head, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException {
        pageExcelWriterParallel(EasyExcelFactory.write(file, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), DEF_PARALLEL_NUM, totalPage, pageListFunction);
    }

    public static <T> void writeForParallel(String pathName, Class<T> head, int parallelNum, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException {
        pageExcelWriterParallel(EasyExcelFactory.write(pathName, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), parallelNum, totalPage, pageListFunction);
    }

    public static <T> void writeForParallel(File file, Class<T> head, int parallelNum, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException {
        pageExcelWriterParallel(EasyExcelFactory.write(file, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), parallelNum, totalPage, pageListFunction);
    }

    public static <T> void writeForParallel(OutputStream outputStream, Class<T> head, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException {
        pageExcelWriterParallel(EasyExcelFactory.write(outputStream, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), DEF_PARALLEL_NUM, totalPage, pageListFunction);
    }

    public static <T> void writeForParallel(OutputStream outputStream, Class<T> head, int parallelNum, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException {
        pageExcelWriterParallel(EasyExcelFactory.write(outputStream, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), parallelNum, totalPage, pageListFunction);
    }

    private static <T> void pageExcelWriterParallel(ExcelWriter excelWriter, int parallelNum, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException {
        try (excelWriter) {
            if (totalPage <= 0) { // 如果无待写入的数据则写入标题
                excelWriter.write(Collections.emptyList(), EasyExcelFactory.writerSheet().build());
                return;
            }
            AtomicLong count = new AtomicLong();
            WriteSheet writeSheet = EasyExcelFactory.writerSheet(1, SHEET1).build();
            ParallelUtil.parallel(List.class, parallelNum, totalPage)
                    .asyncProducer(pageListFunction::apply)
                    .syncConsumer(pageList -> {
                        writeSheet.setSheetNo((int) (count.addAndGet(pageList.size()) / EXCEL_SHEET_ROW_MAX_SIZE + 1));
                        writeSheet.setSheetName(SHEET + writeSheet.getSheetNo());
                        excelWriter.write(pageList, writeSheet);
            }).start();
        }
    }

    public static <T> void writeForXParallel(String pathName, Class<T> head, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException, ExecutionException {
        pageExcelWriterXParallel(EasyExcelFactory.write(pathName, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), DEF_PARALLEL_NUM, totalPage, pageListFunction);
    }

    public static <T> void writeForXParallel(File file, Class<T> head, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException, ExecutionException {
        pageExcelWriterXParallel(EasyExcelFactory.write(file, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), DEF_PARALLEL_NUM, totalPage, pageListFunction);
    }

    public static <T> void writeForXParallel(OutputStream outputStream, Class<T> head, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException, ExecutionException {
        pageExcelWriterXParallel(EasyExcelFactory.write(outputStream, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), DEF_PARALLEL_NUM, totalPage, pageListFunction);
    }

    public static <T> void writeForXParallel(String pathName, Class<T> head, int parallelNum, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException, ExecutionException {
        pageExcelWriterXParallel(EasyExcelFactory.write(pathName, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), parallelNum, totalPage, pageListFunction);
    }

    public static <T> void writeForXParallel(File file, Class<T> head, int parallelNum, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException, ExecutionException {
        pageExcelWriterXParallel(EasyExcelFactory.write(file, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), parallelNum, totalPage, pageListFunction);
    }

    public static <T> void writeForXParallel(OutputStream outputStream, Class<T> head, int parallelNum, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException, ExecutionException {
        pageExcelWriterXParallel(EasyExcelFactory.write(outputStream, head).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build(), parallelNum, totalPage, pageListFunction);
    }

    private static <T> void pageExcelWriterXParallel(ExcelWriter excelWriter, int parallelNum, long totalPage, LongFunction<List<T>> pageListFunction) throws InterruptedException, ExecutionException {
        try (excelWriter) {
            // 如果无待写入的数据则写入标题
            if (totalPage <= 0) {
                excelWriter.write(Collections.emptyList(), EasyExcelFactory.writerSheet().build());
                return;
            }
            AtomicLong count = new AtomicLong();
            WriteSheet writeSheet = EasyExcelFactory.writerSheet(1, SHEET1).build();
            SlidingWindow.create(List.class, parallelNum, totalPage)
                    .sendWindow(pageListFunction::apply)
                    .receiveWindow(result -> {
                        writeSheet.setSheetNo((int) (count.addAndGet(result.size()) / EXCEL_SHEET_ROW_MAX_SIZE + 1));
                        writeSheet.setSheetName(SHEET + writeSheet.getSheetNo());
                        excelWriter.write(result, writeSheet);
                    }).start();
        }
    }

    public static <T> void writeImportTemplate(String pathName, Class<T> head) {
        EasyExcelFactory.write(pathName).head(getExcelImportHead(head)).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("导入模板").doWrite(Collections.emptyList());
    }

    public static <T> void writeImportTemplate(File file, Class<T> head) {
        EasyExcelFactory.write(file).head(getExcelImportHead(head)).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("导入模板").doWrite(Collections.emptyList());
    }

    public static <T> void writeImportTemplate(OutputStream outputStream, Class<T> head) {
        EasyExcelFactory.write(outputStream).head(getExcelImportHead(head)).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("导入模板").doWrite(Collections.emptyList());
    }

    private static <T> List<List<String>> getExcelImportHead(Class<T> head) {
        List<List<String>> list = new ArrayList<>();
        for (Field field : head.getDeclaredFields()) {
            if (field.getAnnotation(ExcelProperty.class) != null) {
                String[] strings = field.getAnnotation(ExcelProperty.class) != null ? field.getAnnotation(ExcelProperty.class).value() : new String[]{field.getName()};
                List<String> stringList = Arrays.asList(strings);
                list.add(stringList);
            }
        }
        return list;
    }
}