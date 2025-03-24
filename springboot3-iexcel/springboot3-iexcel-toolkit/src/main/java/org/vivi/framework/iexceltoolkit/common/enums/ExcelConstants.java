package org.vivi.framework.iexceltoolkit.common.enums;

public class ExcelConstants {

    public static final String NO_FILE_PATH = "请传入templatePath，指定导出模板路径";

    public static final String IMPORT_NO_TARGET_PARAM = "excel导入，入参必须含有targetParam值";

    public static final String TYPE_IMPORT = "import";

    public static final String TYPE_DYNAMIC = "dynamic";

    public static final String TYPE_TEMPLATE = "template";

    public static final String DYNAMIC_PARAMS_NO_PASS_CHECK = "动态列导出，两个参数重写方法的参数如下： m(List<Map<String,Object>> data,List<String> headers) ";

    public static final String DYNAMIC_PARAMS_NO_PASS_CHECK2 = "动态列导出，三个参数重写方法的参数如下： m(List<Map<String,Object>> data,List<String> headers),Map<String,Object> params) ";

    public static final String DYNAMIC_PARAMS_NO_PASS_CHECK3 = "动态列导出，重写方法的参数如下至少含两个";

    public static final String TEMPLATE_PARAMS_NO_PASS_CHECK = "模板导出，重写方法的参数如下： m(List<Map<String,Object>> data, Map<String,Object> otherVal) ";

    public static final String IMPORT_PARAMS_NO_PASS_CHECK = "excel导入，重写的方法参数如下：m(List<?> data,String remark),或m(List<?> data) ";

    public static final String IMPORT_NO_ENTITY_CLASS = "excel导入，在重写的方法上@MsExcelRewrite注解中，必须指定entityClass值。为了规范起见，excel导入必须有一个实体类进行映射";


}
