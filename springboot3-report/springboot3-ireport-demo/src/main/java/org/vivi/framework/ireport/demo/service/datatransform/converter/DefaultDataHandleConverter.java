package org.vivi.framework.ireport.demo.service.datatransform.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.annotation.IExcelRewrite;
import org.vivi.framework.ireport.demo.common.config.ICache;
import org.vivi.framework.ireport.demo.common.utils.AssertUtils;
import org.vivi.framework.ireport.demo.common.utils.IExcelUtils;
import org.vivi.framework.ireport.demo.common.utils.IocUtil;
import org.vivi.framework.ireport.demo.report.config.IExportConfig;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.datatransform.ReportDataTransformService;
import org.vivi.framework.ireport.demo.web.dto.DataSearchDto;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.vivi.framework.ireport.demo.common.constant.Constants.*;
import static org.vivi.framework.ireport.demo.common.constant.Message.*;
import static org.vivi.framework.ireport.demo.common.constant.Message.IMPORT_PARAMS_NO_PASS_CHECK;
import static org.vivi.framework.ireport.demo.common.constant.Message.TEMPLATE_PARAMS_NO_PASS_CHECK;

@Slf4j
@Service
public class DefaultDataHandleConverter implements ReportDataTransformService {

    @Autowired
    private DataSetService dataSetService;

    /**
     * cache the first time to parse the class information containing the @IExcelRewrite class annotation
     */
    private static Map<String, Class<?>> classCache = ICache.excelClassCache;
    /**
     * cache the first time to parse the method information containing the @IExcelRewrite annotation in the class
     * key="@IExcelRewrite's targetParam value", value=the method object in the class that has been parsed
     */
    private static Map<String, Method> methodCache = ICache.excelMethodCache;
    /**
     * key="@IExcelRewrite's targetParam value",
     * value=the entity information corresponding to the import
     */
    private static Map<String, Class<?>> importCache = ICache.excelImportCache;

    @Override
    public List<T> transform(DataSearchDto searchDto) {
        List dataList = dataSetService.getAllMapData(searchDto);
        if (dataList == null) dataList = new ArrayList();

        List<String> headList = searchDto.getHeadList();
        if (headList == null) headList = new ArrayList<>();

        //进行动态数据重构处理判断
        List newDataList = (headList.size() != 0 && headList.size() != 0) ? IExcelUtils.restructureDynamicData(headList, dataList) : new ArrayList();

        // get export configuration
        IExportConfig config = searchDto.getExportConfig();
        if (config != null) {
            String targetParam = config.getTargetParam();
            //判断是否进行重写数据
            if (StringUtils.isNotBlank(targetParam)) {
                // invoke dynamic
                invokeCache(targetParam, TYPE_DYNAMIC);
                invokeDynamic(targetParam, newDataList, headList, searchDto.getParams());
            }
        }

        //重构表头,去除@符号
        if (headList.size() != 0) {
            headList = headList.stream().map(t -> {
                if (t.contains("@")) {
                    return t.split("@")[0];
                }
                return t;
            }).collect(Collectors.toList());
        }

        searchDto.setExportConfig(config);

        return newDataList;
    }

    private static void invokeCache(String targetParam, String type) {
        if (StringUtils.isNotEmpty(targetParam)){
            if (!targetParam.contains("@")){
                String[] targetParamStr = targetParam.split("@");
                String cName = targetParamStr[0];
                String mName = targetParamStr[1];

                // get class and method from cache
                Class<?> cacheClass = classCache.get(cName);
                Method cacheMethod = methodCache.get(targetParam);

                if (cacheClass == null || cacheMethod == null) {
                    analyses(targetParam, cName, mName, mName);
                }
            }
        }
    }

    private static void analyses(String targetParam, String cPName, String mPName, String type) {
        //获取含有指定注解的类
        Map<String, Object> beansMap = IocUtil.getBeansWithAnnotation(IExcelRewrite.class);
        if (beansMap.size() != 0) {
            for (String key : beansMap.keySet()) {
                Object obj = beansMap.get(key);
                Class<?> serviceClass = obj.getClass();
                //获取类上注解值
                IExcelRewrite classA = IocUtil.findAnnotation(serviceClass, IExcelRewrite.class);
                String cP = classA.targetParam();
                //匹配到对饮更多标识
                if (cPName.equals(cP)) {
                    //缓存类class
                    classCache.put(cPName, serviceClass);
                    //在进一步匹配到方法
                    for (Method method : serviceClass.getMethods()) {
                        //获取每个方法上的注解值,匹配标识
                        IExcelRewrite methodA = IocUtil.findAnnotation(method, IExcelRewrite.class);
                        if (methodA == null) continue;
                        String mP = methodA.targetParam();
                        if (mPName.equals(mP)) {
                            //缓存方法
                            methodCache.put(targetParam, method);
                            //获取方法参数是否与规定的一致
                            checkParameterTypes(method, type);
                            //如果是导入，需要缓存当前的实体信息。
                            cacheCheckImport(type, targetParam, methodA);
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * get cPName
     **/
    private static String cPName(String targetParam) {
        String[] split = targetParam.split("@");
        return split[0];
    }


    /**
     * 调用模板导出
     * 新增有参导出
     **/
    private static void invokeDynamic(String targetParam, List dataList, List<String> headList, Map<String, Object> params){
        try {
            if (params == null) {
                checkMethod(targetParam).invoke(IocUtil.getClassObj(checkClass(cPName(targetParam))), dataList, headList);
            } else {
                checkMethod(targetParam).invoke(IocUtil.getClassObj(checkClass(cPName(targetParam))), dataList, headList, params);
            }
        }catch (Exception e){
            log.error("invoke dynamic method error", e);
        }
    }

    /**
     * invoke template export
     **/
    private static void invokeTemplate(String targetParam, List dataList, Map<String, Object> otherValMap) throws Exception {
        checkMethod(targetParam).invoke(IocUtil.getClassObj(checkClass(cPName(targetParam))), dataList, otherValMap);
    }

    /**
     * 检查是否在class和method正确配置重写方法
     **/
    private static Method checkMethod(String targetParam) {
        Method method = methodCache.get(targetParam);
        if (method == null) {
            AssertUtils.throwException("input targetParam = " + targetParam + ", can not find the corresponding class and method, can not intercept the rewrite. Please check whether the targetParam is configured correctly！");
        }
        return method;
    }

    private static Class<?> checkClass(String targetParam) {
        Class<?> aClass = classCache.get(cPName(targetParam));
        if (aClass == null) {
            AssertUtils.throwException("input targetParam = " + targetParam + ", can not find the corresponding class, can not intercept the rewrite. Please check whether the targetParam is configured correctly！");
        }
        return aClass;
    }


    /**
     * check whether the method parameters are consistent with the specified
     * dynamic export: m(List<Map> data,List<String> headers)
     * template: m(List<Map> data, Map otherValMap)
     * @param method
     * @param type
     */
    public static void checkParameterTypes(Method method, String type) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (TYPE_DYNAMIC.equals(type)) {
            if (parameterTypes.length == 2) {
                if (!parameterTypes[0].getName().equals(List.class.getName()) || !parameterTypes[1].getName().equals(List.class.getName())) {
                    AssertUtils.throwException(DYNAMIC_PARAMS_NO_PASS_CHECK);
                }
            } else if (parameterTypes.length == 3) {
                if (!parameterTypes[0].getName().equals(List.class.getName()) || !parameterTypes[1].getName().equals(List.class.getName()) || !parameterTypes[2].getName().equals(Map.class.getName())) {
                    AssertUtils.throwException(DYNAMIC_PARAMS_NO_PASS_CHECK2);
                }
            } else {
                AssertUtils.throwException(DYNAMIC_PARAMS_NO_PASS_CHECK3);
            }
        }
        if (TYPE_TEMPLATE.equals(type)) {
            if (parameterTypes.length != 2 || !parameterTypes[0].getName().equals(List.class.getName()) || !parameterTypes[1].getName().equals(Map.class.getName())) {
                AssertUtils.throwException(TEMPLATE_PARAMS_NO_PASS_CHECK);
            }
        }
        if (TYPE_IMPORT.equals(type)) {
            if (parameterTypes.length != 1 && parameterTypes.length != 2) {
                AssertUtils.throwException(IMPORT_PARAMS_NO_PASS_CHECK);
            }
            if (parameterTypes.length == 1 && !parameterTypes[0].getName().equals(List.class.getName())) {
                AssertUtils.throwException(IMPORT_PARAMS_NO_PASS_CHECK);
            }
            if (parameterTypes.length == 2 && !parameterTypes[0].getName().equals(List.class.getName()) && !parameterTypes[1].getName().equals(String.class.getName())) {
                AssertUtils.throwException(IMPORT_PARAMS_NO_PASS_CHECK);
            }

        }

    }

    private static void cacheCheckImport(String type, String targetParam, IExcelRewrite methodA) {
        if (TYPE_IMPORT.equals(type)) {
            Class<?> aClass = methodA.entityClass();
            if (aClass.getName().equals(Void.class.getName())) {
                AssertUtils.throwException(IMPORT_NO_ENTITY_CLASS);
            }
            importCache.put(targetParam, aClass);
        }
    }

}
