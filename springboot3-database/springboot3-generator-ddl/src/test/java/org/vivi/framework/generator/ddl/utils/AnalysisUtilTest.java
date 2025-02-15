package org.vivi.framework.generator.ddl.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisUtilTest {

    @Test
    void printDDL() {
    }

    @Test
    void genDDLWithContext() {
        String tableEnglishName = "tableEnglishName";

        // 检查字符串中是否包含"biz"并且后面没有下划线的情况，如果是则在其后添加下划线
        boolean b = tableEnglishName.contains("biz") && !tableEnglishName.substring(tableEnglishName.indexOf("biz") + 3).startsWith("_");
        if (b) {
            int index = tableEnglishName.indexOf("biz") + 3;
            tableEnglishName = tableEnglishName.substring(0, index) + "_" + tableEnglishName.substring(index);
        }
        System.out.println(tableEnglishName);


//        String s = "/Users/zjarlin/ddl的excel模板.xlsx";
//        List<DDLContext> dataFromExcel = getDDLContextFromExcel(s);
//        DDLContext ddlContext = dataFromExcel.get(0);
//        String s1 = genDDLWithContext(ddlContext);
//        System.out.println(s1);
    }

    @Test
    void getDDLContextFromExcel() {
    }
}