package org.vivi.framework.report.bigdata.general;

import cn.hutool.core.lang.Pair;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.vivi.framework.report.bigdata.common.response.R;
import org.vivi.framework.report.bigdata.utils.IocUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GeneralExportService {

    @Autowired
    private ExportExcelManagerProperty exportExcelManagerProperty;

    private static final Map<String, Class> CLASS_MAP = new ConcurrentHashMap<>(32);

    /**
     * 增加导出记录
     *
     * @return
     */
    public Pair<String, byte[]> exportExcel(String type, JSONObject req) {
        ExportExcelManagerProperty.ExportExcelManagerTemplate template = exportExcelManagerProperty
                .getTemplates().get(type);
        Assert.notNull(template, "不支持的导出类型,请联系后端开发配置该类型");
        Class requestBodyClassClass = getClass(template.getRequestBodyClassName());
        Class responseBodyClassClass = getClass(template.getResponseBodyClassName());// 如果当前的response类已存在 @ExcelProperty 注解,则直接使用
        boolean existEasyExcelProperty = hasEasyExcelProperty(responseBodyClassClass);
        Object pageDto = JSONObject.parseObject(req.toJSONString(), requestBodyClassClass);
        int i = 0;
        List<String> header = template.getHeaderFiledMap().stream()
                .map(p -> p.getKey()).collect(Collectors.toList());
        List<String> fields = template.getHeaderFiledMap().stream()
                .map(p -> p.getValue()).collect(Collectors.toList());
        List<Object> data = new LinkedList<>();
        do {
            if (pageDto instanceof PageInfo<?>) {
                ((BasePageDto) pageDto).setPageNum(++i);
                ((BasePageDto) pageDto).setPageSize(1000);
            }  else break;

            String sourceBeanName = template.getBeanName();
            Object sourceBean = sourceBeanName.contains(".") ? IocUtil.getBean(getClass(sourceBeanName)) : IocUtil.getBean(sourceBeanName);
            if (null == sourceBean) {
                throw new RuntimeException("配置的bean不合法");
            }
            Object result = InvokeMethodUtil.invokeMethod(sourceBean, template.getMethodName(), pageDto);
            if (null == result) {
                log.warn("调用bean方法异常! type:{}, req:{}", type, req);
                throw new RuntimeException("配置的bean方法不合法");
            }
            PageInfo<Object> pageData = getRestResponseBody(result);
            // 这里还需要根据header头做映射
            if (existEasyExcelProperty) {
                // 实际结构为 List<Object> ,Object为java类实例
                data.addAll(pageData.getList());
            } else {
                // 实际结构为 List<List<Object>>,Object的原始类型
                data.addAll(handleData(pageData.getList(), responseBodyClassClass, fields, template.getFieldValueMappingFunction()));
            }
            if (!pageData.isHasNextPage()) {
                break;
            }
        } while (i < 1000);
        if (existEasyExcelProperty) {
            return Pair.of(template.getFileName(), writeFile(responseBodyClassClass, data));
        } else {
            return Pair.of(template.getFileName(), writeFile(header, data));
        }
    }

    private static Class getClass(String className) {
        Class tClass = CLASS_MAP.get(className);
        if (tClass != null) {
            return tClass;
        }
        tClass = getClassImpl(className);
        CLASS_MAP.put(className, tClass);
        return tClass;
    }

    private static Class getClassImpl(String className) {
        Class<?> paramClass = null;
        try {
            paramClass = Class.forName(className);
            return paramClass;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("配置的ParamClassName无法识别");
        }
    }

    private boolean hasEasyExcelProperty(Class responseBodyClassClass) {
        Map<Field, ExcelProperty> allField = AnnotationExtentUtil.getAllField(responseBodyClassClass, ExcelProperty.class);
        return allField.size() > 0;
    }

    private static byte[] writeFile(Class responseBodyClassClass, List<Object> data) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ExcelWriter excelWriter = EasyExcelFactory.write().head(responseBodyClassClass).file(os).build();
            WriteSheet writeSheet = EasyExcelFactory.writerSheet(0).build();
            excelWriter.write(data, writeSheet);
            excelWriter.finish();
            return os.toByteArray();
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("生成 excel 异常!", e);
            throw new RuntimeException("生成 excel 异常!");
        }
    }

    private static byte[] writeFile(List<String> headerName, List<Object> data) {
        List<List<String>> header = headerName.stream()
                .map(p -> Collections.singletonList(p))
                .collect(Collectors.toList());
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ExcelWriter excelWriter = EasyExcelFactory.write().head(header).file(os).build();
            WriteSheet writeSheet = EasyExcelFactory.writerSheet(0).build();
            excelWriter.write(data, writeSheet);
            excelWriter.finish();
            return os.toByteArray();
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("生成 excel 异常!", e);
            throw new RuntimeException("生成 excel 异常!");
        }
    }

    /**
     * 对RestResponse做特殊处理，只取其data
     */
    public List<List<Object>> handleData(List<Object> data, Class responseBodyClass, List<String> fields, Map<String, Map<String, String>> functionMap) {

        List<List<Object>> result = new ArrayList<>(data.size());
        for (Object obj : data) {
            List<Object> item = new ArrayList<>(fields.size());
            for (String filed : fields) {
                Object fieldValue = ReflectionUtils.getFieldValue(obj, filed);
                Map<String, String> function = functionMap.get(filed);
                item.add(convertValue(fieldValue, function));
            }
            result.add(item);
        }
        return result;
    }

    private static Object convertValue(Object v, Map<String, String> function) {
        if (v == null) {
            return null;
        }
        if (function == null) {
            if (v instanceof List) {
                return String.join(",", (List) v);
            } else {
                return v;
            }
        }
        if (v instanceof List) {
            return ((List<?>) v).stream()
                    .map(p -> function.containsKey(p.toString())
                            ? function.get(p.toString())
                            : p.toString()
                    )
                    .collect(Collectors.joining(","));
        } else {
            return function.containsKey(v.toString())
                    ? function.get(v.toString())
                    : v;
        }
    }

    /**
     * 对RestResponse做特殊处理，只取其data
     */
    public PageInfo<Object> getRestResponseBody(Object result) {
        if (!(result instanceof R)) {
            throw new RuntimeException("返回类型必须是RestResponse结构");
        }
        R<?> restResponse = (R<?>) result;
        if (R.SUCCESS != restResponse.getCode()) {
            log.error("ApiOperationLogAspect.handleRestResponse resultCode not successful :{}", JSONObject.toJSONString(restResponse));
            return null;
        }
        Object data = restResponse.getData();
        if (null == data) {
            log.error("ApiOperationLogAspect.handleRestResponse RestResponse data is empty");
        }
        if (data instanceof PageInfo) {
            return (PageInfo) (data);
        }
        throw new RuntimeException("返回类型必须是RestResponse<PageInfo<*>>结构");
    }
}
