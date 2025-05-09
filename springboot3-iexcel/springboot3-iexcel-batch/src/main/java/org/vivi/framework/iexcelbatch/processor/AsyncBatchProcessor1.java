package org.vivi.framework.iexcelbatch.processor;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.entity.dto.DataExcelImportDto;
import org.vivi.framework.iexcelbatch.listener.PageReadListener;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Asynchronous batch processor for Excel data import
 */
@Slf4j
@Service
public class AsyncBatchProcessor1 implements AutoCloseable {

    @Autowired
    private BatchDataProcessor1 batchDataProcessor;

    private static final int CORE_POOL_SIZE = 4;
    private static final int MAX_POOL_SIZE = 8;
    private static final int QUEUE_CAPACITY = 8;
    private static final long KEEP_ALIVE_TIME = 60L;

    private final ThreadPoolExecutor executor;

    /**
     * Constructor initializes thread pool
     */
    public AsyncBatchProcessor1() {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        ThreadPoolExecutor.CallerRunsPolicy policy = new ThreadPoolExecutor.CallerRunsPolicy();

        this.executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                workQueue,
                policy
        );

        log.info("Initialized AsyncBatchProcessor with thread pool: core={}, max={}, queueCapacity={}",
                CORE_POOL_SIZE, MAX_POOL_SIZE, QUEUE_CAPACITY);
    }

    /**
     * Read Excel data and process it asynchronously
     *
     * @param head       Excel entity class
     * @param file       Excel file to import
     * @param transformer Function to transform Excel data to entity
     * @param dbFunction  Function to save entities to database
     * @return Import result with success and failure counts
     */
    public <T, R> DataExcelImportDto readExcelAndSaveAsync(
            Class<T> head,
            MultipartFile file,
            Function<T, R> transformer,
            Function<List<R>, Integer> dbFunction) throws IOException {

        validateInputs(head, file, transformer, dbFunction);

        Collection<Future<int[]>> futures = new ArrayList<>();

        // Process Excel file with EasyExcel
        EasyExcel.read(file.getInputStream(), head, new PageReadListener<T>(dataList -> {
            List<R> entities = transformData(dataList, transformer);
            futures.add(batchDataProcessor.saveAsyncBatch(entities, dbFunction));
        })).sheet().doRead();

        return collectResults(futures);
    }

    /**
     * Read Excel data and process it asynchronously using CompletableFuture
     *
     * @param head       Excel entity class
     * @param file       Excel file to import
     * @param transformer Function to transform Excel data to entity
     * @param dbFunction  Function to save entities to database
     * @return Import result with success and failure counts
     */
    public <T, R> DataExcelImportDto readExcelAndSaveAsyncWithCompletableFuture(
            Class<T> head,
            MultipartFile file,
            Function<T, R> transformer,
            Function<List<R>, Integer> dbFunction) throws IOException {

        validateInputs(head, file, transformer, dbFunction);

        Collection<CompletableFuture<int[]>> futures = new ArrayList<>();

        // Process Excel file with EasyExcel
        EasyExcel.read(file.getInputStream(), head, new PageReadListener<T>(dataList -> {
            CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                List<R> entities = transformData(dataList, transformer);
                futures.add(batchDataProcessor.saveAsyncBatch(entities, dbFunction));
            }, executor);

            // Wait for completion of this batch before reading next batch
            task.join();
        })).sheet().doRead();

        return collectResultsFromCompletableFuture(futures);
    }

    /**
     * Process multiple Excel files in parallel
     *
     * @param head       Excel entity class
     * @param files      Array of Excel files to import
     * @param transformer Function to transform Excel data to entity
     * @param dbFunction  Function to save entities to database
     */
    public <T, R> void processMultipleFiles(
            Class<T> head,
            MultipartFile[] files,
            Function<T, R> transformer,
            Function<List<R>, Integer> dbFunction) {

        Assert.notNull(head, "Entity class must not be null");
        Assert.notEmpty(files, "Files must not be empty");
        Assert.notNull(transformer, "Transformer function must not be null");
        Assert.notNull(dbFunction, "Database function must not be null");

        // Process all files in parallel
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                Arrays.stream(files).map(file -> CompletableFuture.runAsync(() -> {
                    try {
                        processExcelFile(head, file, transformer, dbFunction);
                    } catch (IOException e) {
                        log.error("Failed to process file: {}", file.getOriginalFilename(), e);
                    }
                }, executor)).toArray(CompletableFuture[]::new)
        );

        // Wait for all files to be processed
        allTasks.join();
        log.info("Successfully processed {} files", files.length);
    }

    /**
     * Read Excel data and process it asynchronously with dynamic operations
     *
     * @param head       Excel entity class
     * @param file       Excel file to import
     * @param function   Function to transform data using SFunction
     * @return Import result with success and failure counts
     */
    public <T, R> DataExcelImportDto readExcelAndSaveAsyncDynamic(
            Class<T> head,
            MultipartFile file,
            SFunction<T, R> function) throws IOException {

        Assert.notNull(head, "Entity class must not be null");
        Assert.notNull(file, "File must not be null");
        Assert.notNull(function, "Function must not be null");

        Collection<CompletableFuture<int[]>> futures = new ArrayList<>();

        EasyExcel.read(file.getInputStream(), head, new PageReadListener<T>(dataList -> {
            CompletableFuture.runAsync(() -> {
                List<R> entities = dataList.parallelStream().map(function).collect(Collectors.toList());
                futures.add(batchDataProcessor.saveAsyncBatchDynamic(entities, head));
            }, executor).join();
        })).sheet().doRead();

        return collectResultsFromCompletableFuture(futures);
    }

    /**
     * Process a single Excel file
     */
    private <T, R> void processExcelFile(
            Class<T> head,
            MultipartFile file,
            Function<T, R> transformer,
            Function<List<R>, Integer> dbFunction) throws IOException {

        EasyExcel.read(file.getInputStream(), head, new PageReadListener<T>(dataList -> {
            List<R> entities = transformData(dataList, transformer);
            batchDataProcessor.saveAsyncBatch(entities, dbFunction);
        })).sheet().doRead();
    }

    /**
     * Transform data using provided function
     */
    private <T, R> List<R> transformData(List<T> dataList, Function<T, R> transformer) {
        return dataList.parallelStream()
                .map(transformer)
                .collect(Collectors.toList());
    }

    /**
     * Collect results from futures
     */
    private DataExcelImportDto collectResults(Collection<Future<int[]>> futures) {
        int successCount = 0;
        int failCount = 0;

        for (Future<int[]> future : futures) {
            try {
                int[] counts = future.get();
                successCount += counts[0];
                failCount += counts[1];
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error collecting results from future", e);
                Thread.currentThread().interrupt();
            }
        }

        log.info("Import completed. Success: {}, Failed: {}", successCount, failCount);
        return DataExcelImportDto.builder()
                .successCount(successCount)
                .failCount(failCount)
                .build();
    }

    /**
     * Collect results from CompletableFuture
     */
    private DataExcelImportDto collectResultsFromCompletableFuture(Collection<CompletableFuture<int[]>> futures) {
        int[] results = new int[2]; // [successCount, failCount]

        futures.forEach(future -> {
            try {
                int[] counts = future.join();
                results[0] += counts[0]; // success
                results[1] += counts[1]; // failure
            } catch (Exception e) {
                log.error("Error collecting results from CompletableFuture", e);
            }
        });

        log.info("Import completed. Success: {}, Failed: {}", results[0], results[1]);
        return DataExcelImportDto.builder()
                .successCount(results[0])
                .failCount(results[1])
                .build();
    }

    /**
     * Validate input parameters
     */
    private <T, R> void validateInputs(
            Class<T> head,
            MultipartFile file,
            Function<T, R> transformer,
            Function<List<R>, Integer> dbFunction) {

        Assert.notNull(head, "Entity class must not be null");
        Assert.notNull(file, "File must not be null");
        Assert.notNull(transformer, "Transformer function must not be null");
        Assert.notNull(dbFunction, "Database function must not be null");

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
    }

    /**
     * Clean up resources when bean is destroyed
     */
    @PreDestroy
    @Override
    public void close() {
        if (executor != null && !executor.isShutdown()) {
            log.info("Shutting down AsyncBatchProcessor executor");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    log.warn("Executor did not terminate in the specified time.");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.error("Executor shutdown interrupted", e);
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
