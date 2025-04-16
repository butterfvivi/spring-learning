package org.vivi.framework.ireport.demo.excel.achieve;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.ireport.demo.common.annotation.IToolKit;
import org.vivi.framework.ireport.demo.common.config.ICache;
import org.vivi.framework.ireport.demo.common.annotation.IExcelRewrite;
import org.vivi.framework.ireport.demo.service.ReportDataStrategy;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;
import org.vivi.framework.ireport.demo.excel.config.IExportConfig;
import org.vivi.framework.ireport.demo.web.request.ITemplateExportDto;
import org.vivi.framework.ireport.demo.common.utils.AssertUtils;
import org.vivi.framework.ireport.demo.common.utils.EmptyUtils;
import org.vivi.framework.ireport.demo.common.utils.IExcelUtils;
import org.vivi.framework.ireport.demo.common.utils.IocUtil;
import org.vivi.framework.ireport.demo.web.request.ImportExcelDto;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.vivi.framework.ireport.demo.common.constant.Constants.*;
import static org.vivi.framework.ireport.demo.common.constant.Message.*;

@Service
public class ExcelInvokeCore {

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

    @Autowired
    private ReportDataStrategy reportDataStrategy;

    @IToolKit
    public void dynamicExport(HttpServletResponse response, IDynamicExportDto exportDto) throws Exception {
        IDynamicExportDto dto = reportDataStrategy.transform(exportDto.getReportDto(), exportDto);

        List dataList = dto.getDataList();
        if (dataList == null) dataList = new ArrayList();

        List<String> headList = dto.getHeadList();
        if (headList == null) headList = new ArrayList<>();

        // restructure head list by
        List newDataList = (headList.size() != 0 && headList.size() != 0) ?
                IExcelUtils.restructureDynamicData(headList, dataList) : new ArrayList();

        // get export configuration
        IExportConfig config = dto.getConfig();
        if (config != null) {
            String targetParam = config.getTargetParam();
            //判断是否进行重写数据
            if (StringUtils.isNotBlank(targetParam)) {
                // invoke dynamic
                invokeCache(targetParam, TYPE_DYNAMIC);
                invokeDynamic(targetParam, newDataList, headList, dto.getParams());
            }
        }

        if (headList.size() == 0) {
            headList = headList.stream().map(t-> {
                if (t.contains("@")){
                    return t.split("@")[0];
                }
                return t;
            }).collect(Collectors.toList());
        }

        IExcelUtils.writerDynamicToWeb(response, headList, newDataList, config);
    }

    @IToolKit
    public void templateExport(HttpServletResponse response, ITemplateExportDto dto) throws Exception  {
        String templatePath = dto.getTemplatePath();
        AssertUtils.objIsNull(templatePath, NO_FILE_PATH);
        List dataList = EmptyUtils.ifNullSetDefVal(dto.getDataList(), new ArrayList());

        Map<String, Object> otherValMap = EmptyUtils.ifNullSetDefVal(dto.getOtherVal(), new HashMap<>());
        IExportConfig config = dto.getConfig();

        if (config != null) {
            //判断是否进行重写数据
            String targetParam = config.getTargetParam();
            if (StringUtils.isNotEmpty(targetParam)) {
                invokeCache(config.getTargetParam(), TYPE_TEMPLATE);
                invokeTemplate(config.getTargetParam(), dataList, otherValMap);
            }
        }
        //execute excel export
        IExcelUtils.writerTemplateToWeb(response, dataList, templatePath, otherValMap, config);
    }

    /**
     * import data analysis, must have an entity class, the purpose is to facilitate maintenance in the future, and parse data conveniently
     * @param file
     * @param dto  {"targetParam":"","headRow":头部占几行}
     * @return 返回数据取决于用户自己定义
     */
    @IToolKit
    public Object importExcel(MultipartFile file, ImportExcelDto dto) throws Exception {
        String targetParam = dto.getTargetParam();
        AssertUtils.objIsNull(targetParam, IMPORT_NO_TARGET_PARAM);
        Integer headRow = dto.getHeadRow() == null ? 0 : dto.getHeadRow();
        invokeCache(targetParam, TYPE_IMPORT);
        List<?> result = IExcelUtils.readExcelBean(importCache.get(targetParam), file, headRow);
        Object data = invokeImport(targetParam, result, dto);
        return data;
    }

    private static void invokeCache(String targetParam, String type) throws Exception {
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

    /**
     * invoke template export
     **/
    private static void invokeTemplate(String targetParam, List dataList, Map<String, Object> otherValMap) throws Exception {
        checkMethod(targetParam).invoke(IocUtil.getClassObj(checkClass(cPName(targetParam))), dataList, otherValMap);
    }

    /**
     * 调用模板导出
     * 新增有参导出
     **/
    private static void invokeDynamic(String targetParam, List dataList, List<String> headList, Map<String, Object> params) throws Exception {
        if (params == null) {
            checkMethod(targetParam).invoke(IocUtil.getClassObj(checkClass(cPName(targetParam))), dataList, headList);
        } else {
            checkMethod(targetParam).invoke(IocUtil.getClassObj(checkClass(cPName(targetParam))), dataList, headList, params);
        }
    }

    /**
     * 导入
     **/
    private static Object invokeImport(String targetParam, List<?> dataList, ImportExcelDto dto) throws Exception {
        Method method = checkMethod(targetParam);
        //if 2 parameters, the second parameter is the import configuration
        int params = method.getParameters().length;
        if (params == 1) {
            return checkMethod(targetParam).invoke(IocUtil.getClassObj(checkClass(cPName(targetParam))), dataList);
        }
        return method.invoke(IocUtil.getClassObj(checkClass(cPName(targetParam))), dataList, dto.getRemark());
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

    /**
     * get cPName
     **/
    private static String cPName(String targetParam) {
        String[] split = targetParam.split("@");
        return split[0];
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
