package org.vivi.framework.iexcelbatch.processor;

import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.entity.dto.DataExcelImportDto;
import org.vivi.framework.iexcelbatch.listener.PageReadListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AsyncBatchProcessor {

    @Autowired
    private BatchDataProcessor batchDataProcessor;
    /**
     * 异步多线程导入数据
     * 采用自定义注入mybatis-plus的SQL注入器，实现真正的BatchInsert，但是需要注意的是项目配置文件需要在jdbc的url后面加上rewriteBatchedStatements=true
     * @param head  Excel导入实体类的class
     * @param file  要导入的Excel文件
     * @param function  数据处理函数，对数据加工
     * @param dbFunction  数据库操作
     * @return 导入结果
     * @param <T>  Excel导入实体类   例如DataSwMbqcdssImportExcelVO
     * @param <R>  数据库实体类  例如DataSwMbqcdssDO
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public  <T,R> DataExcelImportDto readExcelAndSaveAsync(Class<T> head, MultipartFile file, Function<T,R> function, Function<List<R>,Integer> dbFunction) throws IOException, ExecutionException, InterruptedException {
        Integer successCount = 0;
        Integer failCount = 0;
        //存储异步线程的执行结果
        Collection<Future<int[]>> futures = new ArrayList<>();

        EasyExcel.read(file.getInputStream(), head, new PageReadListener<T>(dataList -> {
            //转换DO，并设置数据源id
            List<R> list = dataList.parallelStream().map(function).collect(Collectors.toList());
            //异步批量插入
            futures.add(batchDataProcessor.saveAsyncBatch(list,dbFunction));
        })).sheet().doRead();
        //等待异步线程执行完毕
        for (Future<int[]> future : futures) {
            int[] counts = future.get();
            successCount += counts[0];
            failCount += counts[1];
        }
        log.info("存储成功总数据量：{},存储失败总数据量:{}", successCount,failCount);
        DataExcelImportDto respVO = DataExcelImportDto.builder().successCount(successCount).failCount(failCount).build();
        return respVO;
    }

    public <T,R> DataExcelImportDto readExcelAndSaveAsync2(Class<T> head,  MultipartFile file,  Function<T,R> function, Function<List<R>,Integer> dbFunction) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Integer successCount = 0;
        Integer failCount = 0;

        List<String> errorNames = Lists.newCopyOnWriteArrayList();
        //String userName = SecurityAuthorHolder.getSecurityUser().getUsername();
        Collection<CompletableFuture<int[]>> allFutures = new ArrayList<>();
        EasyExcel.read(file.getInputStream(),head,new PageReadListener<T>(dataList -> {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(
                            () -> {
                                List<R> list = dataList.parallelStream().map(function).collect(Collectors.toList());
                                allFutures.add(batchDataProcessor.saveAsyncBatch2(list,dbFunction));
                            }, executor)
            );
        })).sheet().doRead();

        for (CompletableFuture<int[]> future : allFutures) {
            int[] counts = future.join();
            successCount += counts[0];
            failCount += counts[1];
        }
        DataExcelImportDto respVO = DataExcelImportDto.builder().successCount(successCount).failCount(failCount).build();
        // 等待所有 CompletableFuture 完成
        //allFutures.join();
        // 关闭线程池
        executor.shutdown();
        return respVO;
    }

    public <T,R> void readExcelAndSaveAsync3(Class<T> head, MultipartFile[] files, Function<T, R> function, Function<List<R>,Integer> dbFunction)  {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                Arrays.stream(files).map(v ->
                        CompletableFuture.runAsync(
                                () -> {
                                    try {
                                        EasyExcel.read(v.getInputStream(), head, new PageReadListener<T>(dataList -> {
                                            //转换DO，并设置数据源id
                                            List<R> list = dataList.parallelStream().map(function).collect(Collectors.toList());
                                            //异步批量插入
                                            batchDataProcessor.saveAsyncBatch(list,dbFunction);
                                        })).sheet().doRead();
                                    } catch (IOException e) {
                                    }
                                }, executor)
                ).toArray(CompletableFuture[]::new)
        );

        // 等待所有 CompletableFuture 完成
        allFutures.join();
        // 关闭线程池
        executor.shutdown();
    }


}
