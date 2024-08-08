package org.vivi.framework.iexcelbatch.common.utils;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.exception.BizException;
import org.vivi.framework.iexcelbatch.entity.dto.DynamicSqlDTO;
import org.vivi.framework.iexcelbatch.listener.PageReadListener;
import org.vivi.framework.iexcelbatch.mapper.CustomSqlMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class EasyExcelUtil2 {

    /**
     * @describe 封装成批量插入的参数对象
     * @Param clazz
     * @Param dataList
     */
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


    /**
     * @return boolean
     * @describe EasyExcel公用导入方法(按日期覆盖)
     * @Param file             excel文件
     * @Param date             数据日期
     * @Param function         数据日期字段的get方法  如传入了date,则需要设置
     * @Param setCreateDate    数据日期set方法       如传入了date,则需要设置
     * @Param mapper           实体类对应的mapper对象 如传入了date,则需要设置
     * @Param entityClass      实体类class
     */
    public static <T> Boolean importExcel(MultipartFile file, Integer date, SFunction<T, Integer> getCreateDate, BiConsumer<T, Integer> setCreateDate, BaseMapper<T> mapper, Class<T> entityClass) {
        //String userName = SecurityAuthorHolder.getSecurityUser().getUsername();
        LocalDateTime now = LocalDateTime.now();
        CustomSqlMapper customSqlService = IocUtil.getBean(CustomSqlMapper.class);

        //根据date来判断  为null则需要删除全表数据  否则删除当天数据
        if (date == null) {
            customSqlService.truncateTable(entityClass.getAnnotation(TableName.class).value());
        } else {
            mapper.delete(Wrappers.lambdaQuery(entityClass).eq(getCreateDate, date));
        }

        try {
            //Method setCreateUser = entityClass.getMethod("setCreateUser", String.class);
            Method setCreateTime = entityClass.getMethod("setCreateTime", LocalDateTime.class);

            EasyExcel.read(file.getInputStream(), entityClass, new PageReadListener<T>(
                    dataList -> {
                        dataList.forEach(v -> {
                            try {
                                //setCreateUser.invoke(v, userName);
                                setCreateTime.invoke(v, now);
                                if (setCreateDate != null) {
                                    setCreateDate.accept(v, date);
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                        if (CollectionUtil.isNotEmpty(dataList)) {
                            customSqlService.executeCustomSql(dynamicSql(entityClass, dataList));
                        }
                    }
            )).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("读取异常");
        }
        return true;
    }

    /**
     * @return boolean
     * @describe EasyExcel公用导入方法(全表覆盖)
     * @Param file
     * @Param entityClass
     */
    public static <T> Boolean importExcel(MultipartFile file, Class<T> entityClass) {
        return importExcel(file, null, null, null, null, entityClass);
    }

    /**
     * @return void
     * @describe EasyExcel公用导出方法
     * @Param clazz
     * @Param dataList
     */
    public static <T> void exportExcel(Class<T> clazz, List<T> dataList) {
        //HttpServletResponse response = ServletRequestUtil.getHttpServletResponse();
        try {
            //EasyExcel.write(response.getOutputStream(), clazz)
            //        .sheet()
            //        .doWrite(() -> dataList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("导出失败");
        }
    }
}
