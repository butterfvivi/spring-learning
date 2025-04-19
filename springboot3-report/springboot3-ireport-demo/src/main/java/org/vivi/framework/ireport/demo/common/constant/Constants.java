package org.vivi.framework.ireport.demo.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static String iExcelPath;

    public static String TYPE_DYNAMIC = "dynamic";

    public static final String TYPE_IMPORT = "import";

    public static final String TYPE_TEMPLATE = "template";

    public static byte[] buffer = new byte[1024 * 1024 * 10];

    /**
     * CURRENT_DATE : 当前日期用字符串
     */
    public static final String CURRENT_DATE = "current";

    /**
     * ORACLE_START : oracle查询一条数据前半部分
     */
    public static final String ORACLE_START = "SELECT * FROM (";

    /**
     * ORACLE_END : oracle查询一条数据后半部分
     */
    public static final String ORACLE_END = ") WHERE ROWNUM = 1";

    @Value("/static/template/")
    public void setIExcelPath(String excelPath) {
        this.iExcelPath = excelPath;
    }
}
