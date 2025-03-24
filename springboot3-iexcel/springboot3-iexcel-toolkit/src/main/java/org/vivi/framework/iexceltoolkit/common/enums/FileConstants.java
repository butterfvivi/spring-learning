package org.vivi.framework.iexceltoolkit.common.enums;

public class FileConstants {

    public static final String NO_PATH = "请传入文件地址！";

    public static final String NO_SUFFIX = "请指定下载的文件后缀！";
    public static final String NO_FILE = "文件不存在！";

    public static final String NO_FILE_LIST = "无文件，无法进行打包下载！";

    public static final String NO_FILE_LIST_SUFFIX = "打包下载，需要定义好每个文件的后缀！";

    public static byte[] buffer = new byte[1024 * 1024 * 10];

    public static final String NO_ANALYSIS_TYPE = "解析.zip压缩包中的文件内容，需要指定解析文件格式，多个文件以逗号拼接，例如：json,txt,xml";
}
