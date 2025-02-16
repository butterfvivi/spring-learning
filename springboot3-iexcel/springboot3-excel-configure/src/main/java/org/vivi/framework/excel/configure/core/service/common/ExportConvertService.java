package org.vivi.framework.excel.configure.core.service.common;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.vivi.framework.excel.configure.common.utils.SpringBeanUtils;
import org.vivi.framework.excel.configure.core.beans.ConverterFieldBean;
import org.vivi.framework.excel.configure.core.beans.ExportFieldBean;
import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * 处理自定义filter
 */
@Service
public class ExportConvertService {

    /**
     * 执行处理自定义的转换器，
     * @param resultObjectList 数据的最终结果
     * @param exportBeanConfig 报表导出上下文
     */
    public void dealMyconverterResult(Object resultObjectList, ExportBeanConfig exportBeanConfig, Map<Integer, ExportFieldBean> exportFieldBeanMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> filterBeanNameList = exportBeanConfig.getConverterList();
        //如果没有定义自定义过滤器则返回
        if(CollectionUtils.isEmpty(filterBeanNameList)){
            return;
        }
        List<ConverterFieldBean> filterFieldBeans = exportBeanConfig.getConverterFieldBeanList();
        for (int i = 0, length = filterBeanNameList.size();i < length;i ++){
            Object object = SpringBeanUtils.getBean(filterBeanNameList.get(i));
            Method method = ReflectionUtils.findMethod(object.getClass(),"convertBatchFieldData", List.class,Map.class, ConverterFieldBean.class);
            if(CollectionUtils.isEmpty(filterFieldBeans)){
                ReflectionUtils.invokeMethod(method,object,resultObjectList,exportFieldBeanMap,null);
            }else {
                if(filterFieldBeans.size() > i){
                    ReflectionUtils.invokeMethod(method,object,resultObjectList,exportFieldBeanMap,filterFieldBeans.get(i));
                }
            }
        }
    }
}
