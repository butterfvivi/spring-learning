package org.vivi.framework.iexcelbatch.processor;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexcelbatch.common.utils.IocUtil;
import org.vivi.framework.iexcelbatch.entity.dto.DynamicSqlDTO;
import org.vivi.framework.iexcelbatch.mapper.CustomSqlMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BatchDataProcessor {

    @Autowired
    private CustomSqlMapper customSqlMapper;

    /**
     * 批量插入
     * @param list  要分批处理的数据
     * @param dbFunction  数据库操作的方法
     * @param <T> 数据库实体类
     * @return 返回处理结果
     */
    @Async
    public <T> Future<int[]> saveAsyncBatch(List<T> list, Function<List<T>,Integer> dbFunction) {
        int size = list.size();
        int[] result = new int[2];
        log.info("saveAsyncBatch当前数据分片大小 size:{}",size);
        try {
            if (dbFunction.apply(list) > 0) {
                result[0] = size;
                log.info("{} 分片存储数据成功,数据量：{}",Thread.currentThread().getName(), size);
            } else {
                result[1] = size;
                log.info("{} 分片存储数据失败：{}",Thread.currentThread().getName(), size);
            }
        }catch (Exception e){
            result[1] = size;
            log.error("{} 分片存储数据出现异常，{}",Thread.currentThread().getName(),e.getMessage());
        }

        return new AsyncResult<int[]>(result);
    }

    /**
     * 批量插入
     * @param list  要分批处理的数据
     * @param dbFunction  数据库操作的方法
     * @param <T> 数据库实体类
     * @return 返回处理结果
     */
    @Async
    public <T> CompletableFuture<int[]> saveAsyncBatch2(List<T> list, Function<List<T>,Integer> dbFunction) {
        int size = list.size();
        int[] result = new int[2];
        log.info("saveAsyncBatch当前数据分片大小 size:{}",size);
        try {
            if (dbFunction.apply(list) > 0) {
                result[0] = size;
                log.info("{} 分片存储数据成功,数据量：{}",Thread.currentThread().getName(), size);
            } else {
                result[1] = size;
                log.info("{} 分片存储数据失败：{}",Thread.currentThread().getName(), size);
            }
        }catch (Exception e){
            result[1] = size;
            log.error("{} 分片存储数据出现异常，{}",Thread.currentThread().getName(),e.getMessage());
        }

        return CompletableFuture.completedFuture(result);
    }

    @Async
    public <T,R> CompletableFuture<int[]> saveAsyncBatchDynamic(List<R> list, Class<T> entityClass) {
        int size = list.size();
        int[] result = new int[2];
        log.info("saveAsyncBatch当前数据分片大小 size:{}",size);
        try {
            //CustomSqlMapper customSqlService = IocUtil.getBean(CustomSqlMapper.class);
            if(customSqlMapper.executeCustomSql(dynamicSql(entityClass, list)) > 0){
                result[0] = size;
                log.info("{} 分片存储数据成功,数据量：{}",Thread.currentThread().getName(), size);
            } else {
                result[1] = size;
                log.info("{} 分片存储数据失败：{}",Thread.currentThread().getName(), size);
            }
        }catch (Exception e){
            result[1] = size;
            log.error("{} 分片存储数据出现异常，{}",Thread.currentThread().getName(),e.getMessage());
        }

        return CompletableFuture.completedFuture(result);
    }

    public static DynamicSqlDTO dynamicSql(Class<?> clazz, List<?> dataList) {
        //字段集合  key=数据库列名  value=实体类get方法
        Map<String, Method> getMethodMap = new LinkedHashMap<>();
        //获取所有字段
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            //获取注解为TableField的字段
            TableField annotation = field.getAnnotation(TableField.class);
            if (annotation != null && annotation.exist()) {
                String column = annotation.value();
                Method getMethod = getGetMethod(clazz, field.getName());
                getMethodMap.put(column, getMethod);
            }
        }

        //value集合
        List<List<Object>> valueList = dataList.stream().map(v -> {
            List<Object> tempList = new ArrayList<>();
            getMethodMap.forEach((key, value) -> {
                try {
                    tempList.add(value.invoke(v));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    tempList.add(null);
                }
            });
            return tempList;
        }).collect(Collectors.toList());

        return DynamicSqlDTO.builder()
                .tableName(clazz.getAnnotation(TableName.class).value())
                .columnList(new ArrayList<>(getMethodMap.keySet()))
                .valueList(valueList)
                .build();
    }

    /**
     * @describe java反射bean的get方法
     * @Param objectClass
     * @Param fieldName
     */
    private static Method getGetMethod(Class<?> objectClass, String fieldName) {
        StringBuilder sb = new StringBuilder();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase(Locale.ROOT));
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getMethod(sb.toString());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Reflect error!");
        }
    }
}
