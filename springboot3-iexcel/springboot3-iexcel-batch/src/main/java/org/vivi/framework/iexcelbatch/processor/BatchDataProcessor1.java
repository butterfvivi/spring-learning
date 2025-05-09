package org.vivi.framework.iexcelbatch.processor;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.vivi.framework.iexcelbatch.entity.dto.DynamicSqlDTO;
import org.vivi.framework.iexcelbatch.mapper.CustomSqlMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Batch data processor for handling asynchronous database operations
 */
@Slf4j
@Service
public class BatchDataProcessor1 {

    @Autowired
    private CustomSqlMapper customSqlMapper;

    /**
     * Asynchronously process a batch of data with the provided database operation function
     *
     * @param list       List of data to be processed
     * @param dbFunction Database operation function that takes a list and returns affected rows
     * @param <T>        Type of the data entity
     * @return CompletableFuture with result array [successCount, failCount]
     */
    @Async
    public <T> CompletableFuture<int[]> saveAsyncBatch(List<T> list, Function<List<T>, Integer> dbFunction) {
        if (CollectionUtils.isEmpty(list)) {
            log.warn("Received empty list for batch processing");
            return CompletableFuture.completedFuture(new int[]{0, 0});
        }

        int size = list.size();
        int[] result = new int[2]; // [successCount, failCount]
        log.info("Processing async batch with size: {}", size);

        try {
            Integer affectedRows = dbFunction.apply(list);
            if (affectedRows != null && affectedRows > 0) {
                result[0] = size;
                log.info("Thread {} successfully processed batch, size: {}", Thread.currentThread().getName(), size);
            } else {
                result[1] = size;
                log.warn("Thread {} failed to process batch, size: {}", Thread.currentThread().getName(), size);
            }
        } catch (Exception e) {
            result[1] = size;
            log.error("Thread {} encountered exception during batch processing: {}",
                    Thread.currentThread().getName(), e.getMessage(), e);
        }

        return CompletableFuture.completedFuture(result);
    }

    /**
     * Asynchronously process a batch of data using dynamic SQL generation
     *
     * @param list        List of data to be processed
     * @param entityClass Class of the target entity
     * @param <T>         Type of the entity class
     * @param <R>         Type of the input data
     * @return CompletableFuture with result array [successCount, failCount]
     */
    @Async
    public <T, R> CompletableFuture<int[]> saveAsyncBatchDynamic(List<R> list, Class<T> entityClass) {
        if (CollectionUtils.isEmpty(list)) {
            log.warn("Received empty list for dynamic batch processing");
            return CompletableFuture.completedFuture(new int[]{0, 0});
        }

        int size = list.size();
        int[] result = new int[2]; // [successCount, failCount]
        log.info("Processing async dynamic batch with size: {}", size);

        try {
            // Generate dynamic SQL from entity class and data list
            DynamicSqlDTO sqlDto = buildDynamicSql(entityClass, list);

            // Execute the dynamic SQL
            int affectedRows = customSqlMapper.executeCustomSql(sqlDto);

            if (affectedRows > 0) {
                result[0] = size;
                log.info("Thread {} successfully processed dynamic batch, size: {}",
                        Thread.currentThread().getName(), size);
            } else {
                result[1] = size;
                log.warn("Thread {} failed to process dynamic batch, size: {}",
                        Thread.currentThread().getName(), size);
            }
        } catch (Exception e) {
            result[1] = size;
            log.error("Thread {} encountered exception during dynamic batch processing: {}",
                    Thread.currentThread().getName(), e.getMessage(), e);
        }

        return CompletableFuture.completedFuture(result);
    }

    /**
     * Build dynamic SQL DTO from entity class and data list
     *
     * @param clazz    Entity class with TableName and TableField annotations
     * @param dataList List of data objects
     * @return DynamicSqlDTO with table name, column names and values
     */
    public static DynamicSqlDTO buildDynamicSql(Class<?> clazz, List<?> dataList) {
        // Extract field mappings from entity class
        Map<String, Method> columnToGetterMap = extractColumnToGetterMap(clazz);

        // Extract table name from class annotation
        TableName tableAnnotation = clazz.getAnnotation(TableName.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("Entity class must have @TableName annotation");
        }
        String tableName = tableAnnotation.value();

        // Map each data object to a list of column values
        List<List<Object>> valueList = dataList.stream()
                .map(dataObject -> extractValues(dataObject, columnToGetterMap))
                .collect(Collectors.toList());

        // Build and return the DTO
        return DynamicSqlDTO.builder()
                .tableName(tableName)
                .columnList(new ArrayList<>(columnToGetterMap.keySet()))
                .valueList(valueList)
                .build();
    }

    /**
     * Extract column names and their corresponding getter methods from an entity class
     */
    private static Map<String, Method> extractColumnToGetterMap(Class<?> clazz) {
        Map<String, Method> getMethodMap = new LinkedHashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            TableField annotation = field.getAnnotation(TableField.class);

            if (annotation != null && annotation.exist()) {
                String column = annotation.value();
                Method getMethod = getGetMethod(clazz, field.getName());
                getMethodMap.put(column, getMethod);
            }
        }

        return getMethodMap;
    }

    /**
     * Extract values from a data object using getter methods
     */
    private static List<Object> extractValues(Object dataObject, Map<String, Method> columnToGetterMap) {
        List<Object> values = new ArrayList<>();

        for (Method getterMethod : columnToGetterMap.values()) {
            try {
                values.add(getterMethod.invoke(dataObject));
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.warn("Failed to extract value using getter method: {}", getterMethod.getName(), e);
                values.add(null);
            }
        }

        return values;
    }

    /**
     * Get the getter method for a field
     *
     * @param objectClass Class containing the field
     * @param fieldName   Name of the field
     * @return Method object for the getter
     */
    private static Method getGetMethod(Class<?> objectClass, String fieldName) {
        String getterMethodName = "get" +
                fieldName.substring(0, 1).toUpperCase(Locale.ROOT) +
                fieldName.substring(1);

        try {
            return objectClass.getMethod(getterMethodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to find getter method for field: " + fieldName, e);
        }
    }
}