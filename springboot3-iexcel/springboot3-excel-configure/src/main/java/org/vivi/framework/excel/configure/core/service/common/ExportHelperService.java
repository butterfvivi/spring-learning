package org.vivi.framework.excel.configure.core.service.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vivi.framework.excel.configure.base.annotations.ExportField;
import org.vivi.framework.excel.configure.common.exceptions.NoConvertMappingCacheException;
import org.vivi.framework.excel.configure.core.beans.ConverterFieldBean;
import org.vivi.framework.excel.configure.core.beans.ExportFieldBean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExportHelperService {
    private Logger logger = LoggerFactory.getLogger(ExportHelperService.class);


    /**
     * 将查询的数据对象中的属性转换为Map<Integer,String>格式，同时根据提供的字段源数据替换数据
     *
     * @param list
     * @return List<Map<Integer,String>>
     * @throws Exception
     */
    public List<Map<Integer, String>> getConvertRowDataMap(List list, Map<String, ExportFieldBean> exportFieldBeanMap, Set<String> needConvertFieldIndexSet) throws Exception {
        List<Map<String, Object>> sourceMapList = (List<Map<String, Object>>)list;
        List<Map<Integer, String>> resultMapList = new ArrayList(list.size());
        for (Map<String, Object> rowMap : sourceMapList) {
            Map<Integer, String> newRowMap = new HashMap<>(rowMap.size());
            for (Map.Entry<String, Object> entry : rowMap.entrySet()) {
                ExportFieldBean exportFieldBean = exportFieldBeanMap.get(entry.getKey());
                if (exportFieldBean == null) {
                    throw new NoConvertMappingCacheException("the column " + entry.getKey() + " has not match ExportFieldBean .");
                }

                if(entry.getValue() == null){
                    newRowMap.put(exportFieldBean.getIndex(), "");
                    continue;
                }
                if(StringUtils.isEmpty(exportFieldBean.getFormate())) {
                    newRowMap.put(exportFieldBean.getIndex(), entry.getValue().toString());
                    continue;
                }
                else {
                    SimpleDateFormat sdf = new SimpleDateFormat(exportFieldBean.getFormate());
                    Date date = sdf.parse(entry.getValue().toString());
                    String newDate = sdf.format(date);
                    newRowMap.put(exportFieldBean.getIndex(), newDate);
                }
            }
            resultMapList.add(newRowMap);
        }
        return resultMapList;
    }

    /**
     * 将查询的数据对象中的属性转换为Map<Integer,String>格式，同时根据提供的字段源数据替换数据
     *
     * @param object
     * @param converterFieldBean
     * @return
     * @throws Exception
     */
    public void getConvertRowDataObject(Object object, ConverterFieldBean converterFieldBean) throws Exception {
        Map<String,Map<String,String>> queryCacheMap = converterFieldBean.getSourceLinkedMap();
        Field[] fields = object.getClass().getDeclaredFields();

        Set<String> needConvertFieldIndexSet = converterFieldBean.getSourceLinkedSet();

        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            if (f.isAnnotationPresent(ExportField.class)) {
                ExportField exportField = f.getAnnotation(ExportField.class);
                if(!needConvertFieldIndexSet.contains(exportField.index()+"")){
                    continue;
                }
                String getMethodName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                Method method = object.getClass().getMethod(getMethodName);
                Object value = method.invoke(object);
                if (value == null) {
                    continue;
                }
                if (StringUtils.isNotEmpty(exportField.sourceKey())) {
                    if(queryCacheMap == null){
                        String msg = f.getName()+"'s sourceKey is not null, but queryCacheMap is null !";
                        throw new NoConvertMappingCacheException(msg);
                    }
                    Map<String, String> valueMap = queryCacheMap.get(exportField.sourceKey());
                    String referValue = valueMap.get(value.toString());
                    if(StringUtils.isEmpty(referValue)){
                        referValue = "";
                    }
                    //说明保留原字段且新增引用字段
                    if (exportField.referIndex() >= 0) {
                        int fieldIndex = getFieldIndex(fields, exportField.referIndex());
                        setFieldValue2(referValue, fields[fieldIndex], object);
                    } else {
                        setFieldValue2(referValue, f, object);
                    }
                }

                if(StringUtils.isNotEmpty(exportField.formate())){
                    SimpleDateFormat sdf = new SimpleDateFormat(exportField.formate());
                    Date date = sdf.parse(value.toString());
                    String newDate = sdf.format(date);
                    setFieldValue2(newDate, f, object);
                }
            }
        }
    }

    private int getFieldIndex(Field[] fields, int referIndex) {
        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            if (f.isAnnotationPresent(ExportField.class)) {
                ExportField exportField = f.getAnnotation(ExportField.class);
                if (exportField.index() == referIndex) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * 反射给目标实例对象instance的field属性设置value
     * @param value
     * @param field
     * @param instance
     * @throws Exception
     */
    private void setFieldValue2(Object value, Field field, Object instance) throws Exception {

        try {
            Method method;
            field.setAccessible(true);
            if (value == null) {
                value = "";
            }
            String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            if (field.getType() == String.class) {
                method = instance.getClass().getMethod(methodName, String.class);
                method.invoke(instance, value.toString());
            } else if (Date.class == field.getType()) {
                method = instance.getClass().getMethod(methodName, Date.class);
                method.invoke(instance, (Date) value);
            } else {
                method = instance.getClass().getMethod(methodName,field.getType().getClass());
                method.invoke(instance, value.toString());
                logger.warn("类型不匹配.........................................");
            }

        } catch (IllegalAccessException e) {
            logger.error("importStaff IllegalAccessException ------:", e);
        } catch (InvocationTargetException e) {
            logger.error("importStaff InvocationTargetException ------:", e);
        }
    }



}
