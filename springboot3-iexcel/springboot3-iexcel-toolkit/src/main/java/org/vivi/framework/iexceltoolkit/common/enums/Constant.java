package org.vivi.framework.iexceltoolkit.common.enums;

import org.springframework.beans.factory.annotation.Value;

public class Constant {

    public static Long batchDataSize=40000L;

    /**
     * 成功标记
     */
    public static Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static Integer FAIL = 500;

    //存放模板路径
    public static String ExcelPath;

    //--------下面三个变量仅仅针对utils.toolkit.achieve包
    //根据这三个变量，判断工具包是否能正常启用，首先msUtEnable控制工具包的bean能否正常注册到ioc容器，
    //msUtEmpty1 || msUtEmpty2 可以控制指定方法是否生效
    public static String MS_UT_ENABLE = "msUtEnable";
    public static String MS_UT_EMPTY1 = "msUtEmpty1";
    public static String MS_UT_EMPTY2 = "msUtEmpty2";

    @Value("/template/}")
    public void setExcelPath(String excelPath) {
        this.ExcelPath = excelPath;
    }
}
