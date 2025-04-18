package org.vivi.framework.ireport.demo.common.constant;

public class Message {

    public static final String DYNAMIC_PARAMS_NO_PASS_CHECK = "dynamic column export, the method parameters do not meet the requirements";

    public static final String DYNAMIC_PARAMS_NO_PASS_CHECK2 = "The method parameters do not meet the requirements of dynamic export";

    public static final String DYNAMIC_PARAMS_NO_PASS_CHECK3 = "The method parameters do not meet the requirements of dynamic export";

    public static final String TEMPLATE_PARAMS_NO_PASS_CHECK = "The method parameters do not meet the requirements of template export";

    public static final String IMPORT_PARAMS_NO_PASS_CHECK = "The method parameters do not meet the requirements of import";

    public static final String IMPORT_NO_TARGET_PARAM = "excel导入，入参必须含有targetParam值";

    public static final String IMPORT_NO_ENTITY_CLASS = "excel导入，在重写的方法上@MsExcelRewrite注解中，必须指定entityClass值。为了规范起见，excel导入必须有一个实体类进行映射";

    public static final String NO_FILE_PATH = "please specify the file path";

    public static final String NO_PATH = "please indicate the file path";

    public static final String NO_SUFFIX = "please specify the file suffix";

    public static final String NO_FILE = "File not found!";


    public static final String NO_FILE_LIST = "无文件，无法进行打包下载！";

    public static final String NO_FILE_LIST_SUFFIX = "打包下载，需要定义好每个文件的后缀！";

    public static final String NO_ANALYSIS_TYPE = "解析.zip压缩包中的文件内容，需要指定解析文件格式，多个文件以逗号拼接，例如：json,txt,xml";
}
