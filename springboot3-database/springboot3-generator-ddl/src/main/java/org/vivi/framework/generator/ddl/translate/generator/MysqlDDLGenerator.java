package org.vivi.framework.generator.ddl.translate.generator;

import cn.hutool.core.util.StrUtil;
import org.vivi.framework.generator.ddl.translate.entity.DDLContext;
import org.vivi.framework.generator.ddl.translate.entity.RangeContext;
import org.vivi.framework.generator.ddl.utils.JlStrUtil;
import org.vivi.framework.generator.ddl.utils.AnalysisUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MysqlDDLGenerator extends DatabaseDDLGenerator{
    @Override
    public String generateDDL(DDLContext ddlContext) {
        String sheetName = ddlContext.getSheetName();
        String tableEnglishName = ddlContext.getTableEnglishName();
        String tableChineseName = ddlContext.getTableChineseName();
        String databaseType = ddlContext.getDatabaseType();
        List<RangeContext> dto = ddlContext.getDto();

        String lineSeparator = System.lineSeparator();


        System.out.println("#------------开始解析mysql语法--------------当前读取的sheet页：" + sheetName + "--------------------------");
        String join = StrUtil.join(" ", "CREATE TABLE", "`" + tableEnglishName + "` (", lineSeparator);


        final StringBuilder[] finalDdl = {new StringBuilder()};

        AtomicBoolean autoIncrement = new AtomicBoolean(true);
        String collect = dto.stream().map(e -> {

            String fieldName = e.getFieldName();
            String fieldComment = e.getFieldComment();
            String fieldCommentAppend = e.getFieldCommentAppend();
            String syndicationNotes = e.getSyndicationNotes();
            String fieldType = e.getFieldType();
            String fieldLength = e.getFieldLength();
            String isPrimaryKey = e.getIsPrimaryKey();
            String isSelfIncreasing = e.getIsSelfIncreasing();


            autoIncrement.set("Y".equals(isSelfIncreasing));

            String s = JlStrUtil.makeSurroundWith(fieldName, "`");
            String s1 = !"0".equals(fieldLength) ? "(" + fieldLength + ")" : "";
            String s2 = "Y".equals(isPrimaryKey) ? " PRIMARY KEY " : "";
            String s3 = "Y".equals(isSelfIncreasing) ? " AUTO_INCREMENT " : "";
            String s4 = "COMMENT '" + syndicationNotes + "'";
            String join1 = StrUtil.join(" ", s, fieldType, s1, s2, s3, s4);


            return join1;
        }).collect(Collectors.joining("," + System.lineSeparator()));


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(") ENGINE=InnoDB ").append(autoIncrement.get() ? "AUTO_INCREMENT=1" : "").append(" DEFAULT CHARSET=utf8mb4 ").append(!"".equals(tableChineseName) ? "COMMENT = '" + AnalysisUtil.removeNotChinese(tableChineseName) + "'" : "").append(";\r\n");
        stringBuilder.append("----------------------------------------------------------------------------------\r\n");

        return join + collect + stringBuilder.toString();
    }
}
