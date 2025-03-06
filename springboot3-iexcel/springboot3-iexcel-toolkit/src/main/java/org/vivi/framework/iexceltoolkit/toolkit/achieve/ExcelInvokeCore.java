package org.vivi.framework.iexceltoolkit.toolkit.achieve;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexceltoolkit.common.utils.AssertUtil;
import org.vivi.framework.iexceltoolkit.common.utils.EmptyUtils;
import org.vivi.framework.iexceltoolkit.common.utils.IocUtil;
import org.vivi.framework.iexceltoolkit.toolkit.dto.IExportConfig;
import org.vivi.framework.iexceltoolkit.web.request.IDynamicExportReq;
import org.vivi.framework.iexceltoolkit.web.request.ITemplateExportReq;
import org.vivi.framework.iexceltoolkit.web.request.ImportReq;
import org.vivi.framework.iexceltoolkit.toolkit.annotation.IExcelRewrite;
import org.vivi.framework.iexceltoolkit.toolkit.annotation.IToolKit;
import org.vivi.framework.iexceltoolkit.toolkit.cache.MCache;
import org.vivi.framework.iexceltoolkit.toolkit.utils.ExcelUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "excelInvokeCore")
public class ExcelInvokeCore {

    private static final String NO_FILE_PATH = "请传入templatePath，指定导出模板路径";
    private static final String IMPORT_NO_TARGET_PARAM = "excel导入，入参必须含有targetParam值";

    private static final String TYPE_IMPORT = "import";

    private static final String TYPE_DYNAMIC = "dynamic";
    private static final String TYPE_TEMPLATE = "template";
    private static final String DYNAMIC_PARAMS_NO_PASS_CHECK = "动态列导出，两个参数重写方法的参数如下： m(List<Map<String,Object>> data,List<String> headers) ";
    private static final String DYNAMIC_PARAMS_NO_PASS_CHECK2 = "动态列导出，三个参数重写方法的参数如下： m(List<Map<String,Object>> data,List<String> headers),Map<String,Object> params) ";
    private static final String DYNAMIC_PARAMS_NO_PASS_CHECK3 = "动态列导出，重写方法的参数如下至少含两个";
    private static final String TEMPLATE_PARAMS_NO_PASS_CHECK = "模板导出，重写方法的参数如下： m(List<Map<String,Object>> data, Map<String,Object> otherVal) ";
    private static final String IMPORT_PARAMS_NO_PASS_CHECK = "excel导入，重写的方法参数如下：m(List<?> data,String remark),或m(List<?> data) ";
    private static final String IMPORT_NO_ENTITY_CLASS = "excel导入，在重写的方法上@MsExcelRewrite注解中，必须指定entityClass值。为了规范起见，excel导入必须有一个实体类进行映射";


    //缓存第一次解析含有@MsExcelRewrite类上注解的class信息
    private static Map<String, Class<?>> classCache = MCache.excelClassCache;
    //缓存第一次解析到class类中含有@MsExcelRewrite注解的方法信息， key="@MsExcelRewrite的targetParam值"，value=所需要的方法对象
    private static Map<String, Method> methodCache = MCache.excelMethodCache;
    //key="@MsExcelRewrite的targetParam值"，value=导入所对应的实体类信息
    private static Map<String, Class<?>> importCache = MCache.excelImportCache;


    @IToolKit
    public void dynamicExport(HttpServletResponse response, IDynamicExportReq req) throws Exception {
        //检验必填
        List dataList = req.getDataList();
        if (dataList == null) dataList = new ArrayList();
        //MsAssertUtils.objIsNull(dataList, NO_DATA);
        List<String> headList = req.getHeadList();
        if (headList == null) headList = new ArrayList<>();
        //MsAssertUtils.objIsNull(headList, NO_HEAD);
        //进行动态数据重构处理判断
        List newDataList = (headList.size() != 0 && headList.size() != 0) ? ExcelUtils.restructureDynamicData(headList, dataList) : new ArrayList();
        //获取配置项,检查是否重写
        IExportConfig config = req.getConfig();
        if (config != null) {
            String targetParam = config.getTargetParam();
            //判断是否进行重写数据
            if (EmptyUtils.isNotEmpty(targetParam)) {
                invokeCache(targetParam, TYPE_DYNAMIC);
                invokeDynamic(targetParam, newDataList, headList, req.getParams());
            }
            //invokeWaterMark(config);
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
        ExcelUtils.writerDynamicToWeb(response, headList, newDataList, config);
    }

    /**
     * 模板导出
     */
    @IToolKit
    public void templateExport(HttpServletResponse response, ITemplateExportReq req) throws Exception {
        //检验必填
        String templatePath = req.getTemplatePath();
        AssertUtil.objIsNull(templatePath, NO_FILE_PATH);
        List dataList = EmptyUtils.ifNullSetDefVal(req.getDataList(), new ArrayList());
        //MsAssertUtils.objIsNull(dataList, NO_DATA);
        Map<String, Object> otherValMap = EmptyUtils.ifNullSetDefVal(req.getOtherVal(), new HashMap<>());
        IExportConfig config = req.getConfig();
        if (config != null) {
            //判断是否进行重写数据
            String targetParam = config.getTargetParam();
            if (EmptyUtils.isNotEmpty(targetParam)) {
                invokeCache(config.getTargetParam(), TYPE_TEMPLATE);
                invokeTemplate(config.getTargetParam(), dataList, otherValMap);
            }
            //invokeWaterMark(config);
        }
        //执行excel导出
        Map<String, List> mutipleTableDatas = req.getMutipleTableDatas();
        if(MapUtils.isNotEmpty(mutipleTableDatas)){
            ExcelUtils.writerTemplateToWeb(response, mutipleTableDatas, templatePath, otherValMap, config);
        }else {
            ExcelUtils.writerTemplateToWeb(response, dataList, templatePath, otherValMap, config);
        }
    }

    /**
     * 调用模板导出
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
     * 导入数据解析，要求必须有一个实体类，目的是为了便于后期人员维护，和解析数据方便
     *
     * @param file
     * @param dto  {"targetParam":"","headRow":头部占几行}
     * @return 返回数据取决于用户自己定义
     * @author mashuai
     */
    @IToolKit
    public Object importExcel(MultipartFile file, ImportReq dto) throws Exception {
        String targetParam = dto.getTargetParam();
        AssertUtil.objIsNull(targetParam, IMPORT_NO_TARGET_PARAM);
        Integer headRow = dto.getHeadRow() == null ? 0 : dto.getHeadRow();
        invokeCache(targetParam, TYPE_IMPORT);
        List<?> result = ExcelUtils.readExcelBean(importCache.get(targetParam), file, headRow);
        Object data = invokeImport(targetParam, result, dto);
        return data;
    }

    /**
     * 缓存需要重写的数据
     **/
    private static void invokeCache(String targetParam, String type) throws Exception {
        if (EmptyUtils.isNotEmpty(targetParam)) {
            if (!targetParam.contains("@")) return;
            String[] split = targetParam.split("@");
            String cPName = split[0];
            String mPName = split[1];
            //从缓存取值，如果没有被缓存，进行class解析
            Class<?> cacheClass = classCache.get(cPName);
            Method cacheMethod = methodCache.get(targetParam);
            if (cacheClass == null || cacheMethod == null) {
                analyses(targetParam, cPName, mPName, type);
            }
        }
    }

    /**
     * 导入
     **/
    private static Object invokeImport(String targetParam, List<?> dataList, ImportReq dto) throws Exception {
        Method method = checkMethod(targetParam);
        //如果有两个参数，则第二个参数为导入配置
        int params = method.getParameters().length;
        if (params == 1) {
            Class<?> aClass = checkClass(cPName(targetParam));
            Object classObj = IocUtil.getClassObj(aClass);
            return checkMethod(targetParam)
                    .invoke(classObj, dataList);
        }
        return method.invoke(IocUtil.getClassObj(checkClass(cPName(targetParam))), dataList, dto.getRemark());
    }

    /**
     * 检查是否在class和method正确配置重写方法
     **/
    private static Method checkMethod(String targetParam) {
        Method method = methodCache.get(targetParam);
        if (method == null) {
            AssertUtil.throwException("传入的targetParam = " + targetParam + "，未能找到对应的class类和方法名，无法进行重写拦截。请检查是否正确配置targetParam！");
        }
        return method;
    }

    private static Class<?> checkClass(String targetParam) {
        Class<?> aClass = classCache.get(cPName(targetParam));
        if (aClass == null) {
            AssertUtil.throwException("传入的targetParam = " + targetParam + "，未能找到对应的class类，无法进行重写拦截。请检查是否正确配置targetParam！");
        }
        return aClass;
    }

    /**
     * 获取cpName
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
                AssertUtil.throwException(IMPORT_NO_ENTITY_CLASS);
            }
            importCache.put(targetParam, aClass);
        }
    }

    /**
     * 检查方法参数是否与规定的一致
     * 动态导出：m(List<Map> data,List<String> headers)
     * 模板：m(List<Map> data, Map otherValMap)
     */

    private static void checkParameterTypes(Method method, String type) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (TYPE_DYNAMIC.equals(type)) {
            if (parameterTypes.length == 2) {
                if (!parameterTypes[0].getName().equals(List.class.getName()) || !parameterTypes[1].getName().equals(List.class.getName())) {
                    AssertUtil.throwException(DYNAMIC_PARAMS_NO_PASS_CHECK);
                }
            } else if (parameterTypes.length == 3) {
                if (!parameterTypes[0].getName().equals(List.class.getName()) || !parameterTypes[1].getName().equals(List.class.getName()) || !parameterTypes[2].getName().equals(Map.class.getName())) {
                    AssertUtil.throwException(DYNAMIC_PARAMS_NO_PASS_CHECK2);
                }
            } else {
                AssertUtil.throwException(DYNAMIC_PARAMS_NO_PASS_CHECK3);
            }
        }
        if (TYPE_TEMPLATE.equals(type)) {
            if (parameterTypes.length != 2 || !parameterTypes[0].getName().equals(List.class.getName()) || !parameterTypes[1].getName().equals(Map.class.getName())) {
                AssertUtil.throwException(TEMPLATE_PARAMS_NO_PASS_CHECK);
            }
        }
        if (TYPE_IMPORT.equals(type)) {
            if (parameterTypes.length != 1 && parameterTypes.length != 2) {
                AssertUtil.throwException(IMPORT_PARAMS_NO_PASS_CHECK);
            }
            if (parameterTypes.length == 1 && !parameterTypes[0].getName().equals(List.class.getName())) {
                AssertUtil.throwException(IMPORT_PARAMS_NO_PASS_CHECK);
            }
            if (parameterTypes.length == 2 && !parameterTypes[0].getName().equals(List.class.getName()) && !parameterTypes[1].getName().equals(String.class.getName())) {
                AssertUtil.throwException(IMPORT_PARAMS_NO_PASS_CHECK);
            }

        }
    }

}
