package org.vivi.framework.iexceltoolkit.common.enums;

public interface Constant {

    Long batchDataSize=40000L;

    /**
     * 成功标记
     */
    Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    Integer FAIL = 500;

    //存放模板路径
    String ExcelPath = "";

    //--------下面三个变量仅仅针对utils.toolkit.achieve包
    //根据这三个变量，判断工具包是否能正常启用，首先msUtEnable控制工具包的bean能否正常注册到ioc容器，
    //msUtEmpty1 || msUtEmpty2 可以控制指定方法是否生效
    String MS_UT_ENABLE = "msUtEnable";
    String MS_UT_EMPTY1 = "msUtEmpty1";
    String MS_UT_EMPTY2 = "msUtEmpty2";
}
