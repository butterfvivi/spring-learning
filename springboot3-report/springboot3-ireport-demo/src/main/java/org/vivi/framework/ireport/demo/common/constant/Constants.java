package org.vivi.framework.ireport.demo.common.constant;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static String iExcelPath;

    public static String TYPE_DYNAMIC = "dynamic";

    public static final String TYPE_IMPORT = "import";

    public static final String TYPE_TEMPLATE = "template";

    public static byte[] buffer = new byte[1024 * 1024 * 10];
}
