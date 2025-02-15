package org.vivi.framework.generator.ddl.translate.translator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransUtilTest {

    @Test
    void chinese2English() {
    }

    @Test
    void testChinese2English() {
        String s = "字段名\t类型\t长度,小数点\t是否为主键\t是否自增\t注释\t追加注释\t示例值";
        String s1 = "主键\n" +
                "创建者\n" +
                "创建时间\n" +
                "更新者\n" +
                "更新时间\n" +
                "逻辑删除字段";
        String s2 = TransUtil.chinese2EnglishByInputRow(s);
        String s3 = TransUtil.chinese2EnglishByInputColumn(s);
        System.out.println(s3);
    }

    @Test
    void testChinese2English1() {
    }
}