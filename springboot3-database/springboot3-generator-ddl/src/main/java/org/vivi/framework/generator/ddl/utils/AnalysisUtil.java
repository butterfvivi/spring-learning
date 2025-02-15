package org.vivi.framework.generator.ddl.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vivi.framework.generator.ddl.translate.entity.DDLContext;
import org.vivi.framework.generator.ddl.translate.entity.RangeContext;
import org.vivi.framework.generator.ddl.translate.generator.DatabaseDDLGenerator;
import org.vivi.framework.generator.ddl.translate.translator.TransUtil;

import java.io.BufferedInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.hutool.core.text.CharSequenceUtil.isBlank;

/**
 * 读取excel文件内容生成数据库表ddl
 */
public class AnalysisUtil {

    public static void printDDL(String filePath) {
        List<DDLContext> ddlContextFromExcel = getDDLContextFromExcel(filePath);
        ddlContextFromExcel.stream().map(e -> genDDLWithContext(e)).forEach(System.out::println);
    }

    public static String genDDLWithContext(DDLContext ddlContext) {
        String databaseType = ddlContext.getDatabaseType();
        DatabaseDDLGenerator databaseDDLGenerator = DatabaseDDLGenerator.databaseType.get(databaseType);
        return databaseDDLGenerator.generateDDL(ddlContext);
    }

    public static List<DDLContext> getDDLContextFromExcel(String filePath) {
        Workbook wookbook = getWorkBookFromPath(filePath);
        int numberOfSheets = wookbook.getNumberOfSheets();
        Workbook finalWookbook = wookbook;
        List<Sheet> collect = Stream.iterate(0, (index) -> ++index).limit(numberOfSheets)
                .filter(e ->
                        !finalWookbook.isSheetHidden(e)
                ).
                map(finalWookbook::getSheetAt).
                collect(Collectors.toList());
              ArrayList<DDLContext> ddlContexts = new ArrayList<>();

        for (Sheet sheet : collect) {
            String ddl = null;
            boolean finished = false;
            StringBuilder ddl1 = new StringBuilder();
            // 是否自增
            boolean autoIncrement = false;
            String sheetName = sheet.getSheetName();
            // 当前读取行的行号
            int rowId = 1;
            Iterator<Row> rows = sheet.rowIterator();

            String tableEnglishName = "";
            DDLContext ddlContext = new DDLContext();
            String tableChineseName = sheetName;
            ddlContext.setSheetName(sheetName);

            List<RangeContext> list = new ArrayList<>();
            //StrUtil.removp
            while (rows.hasNext()) {
                Row row = rows.next();
                //获取表英文名
                if (rowId == 1) {
                    Cell cell1 = row.getCell(0);
                    if (!"表英文名".equals(cell1.getStringCellValue())) {
                        System.out.println("第一行第一格应该为“表英文名”!");
                        finished = true;
                        break;
                    }
                    Cell cell2 = row.getCell(1);
                    tableEnglishName = cell2.getStringCellValue();


                    String databasetype = Optional.ofNullable(row.getCell(3))
                            .map(e -> e.getStringCellValue())
                            .filter(StrUtil::isNotBlank).orElse("mysql");
                    //数据库名称
                    String databaseName = Optional.ofNullable(row.getCell(5))
                            .map(e -> e.getStringCellValue())
                            .filter(StrUtil::isNotBlank).orElse("");

                    ddlContext.setTableEnglishName(tableEnglishName);
                    ddlContext.setDatabaseType(databasetype);

                    ddlContext.setDatabaseName(databaseName);


                    rowId++;
                    continue;
                }
                //获取表中文名
                if (rowId == 2) {
                    Cell cell1 = row.getCell(0);
                    if (!"表中文名".equals(cell1.getStringCellValue())) {
                        System.out.println("第2行第一格应该为“表中文名”!");
                        finished = true;
                        break;
                    }
                    Cell cell2 = row.getCell(1);
                    if (Objects.nonNull(cell2) && StrUtil.isNotBlank(cell2.getStringCellValue())) {
                        tableChineseName = cell2.getStringCellValue();

                        ddlContext.setTableChineseName(tableChineseName);
                    }
                    if (isBlank(tableEnglishName)) {
                        String s = TransUtil.chinese2English(tableChineseName);
                        tableEnglishName = s;
                        ddlContext.setTableChineseName(tableChineseName);

                        // 检查字符串中是否包含"biz"并且后面没有下划线的情况，如果是则在其后添加下划线
                        boolean b = tableEnglishName.contains("biz") && !tableEnglishName.substring(tableEnglishName.indexOf("biz") + 3).startsWith("_");
                        if (b) {
                            int index = tableEnglishName.indexOf("biz") + 3;
                            tableEnglishName = tableEnglishName.substring(0, index) + "_" + tableEnglishName.substring(index);
                        }
                        tableEnglishName = shortEng(tableEnglishName, tableChineseName);
                        ddlContext.setTableEnglishName(tableEnglishName);
                    }
                    rowId++;
                    continue;
                }
                //校验属性列名称和顺序
                if (rowId == 3) {
                    rowId++;
                    continue;
                }
                if (!row.cellIterator().hasNext()) {
                    break;
                }
                //mysql所有保留字
                List<String> strings = Arrays.asList("action", "add", "aggregate", "all", "alter", "after", "and", "as", "asc", "avg", "avg_row_length", "auto_increment"
                        , "between", "bigint", "bit", "binary", "blob", "bool", "both", "by", "cascade", "case", "char", "character", "change", "check"
                        , "checksum", "column", "columns", "comment", "constraint", "create", "cross", "current_date", "current_time", "current_timestamp"
                        , "data", "database", "databases", "date", "datetime", "day", "day_hour", "day_minute", "day_second", "dayofmonth", "dayofweek"
                        , "dayofyear", "dec", "decimal", "default", "delayed", "delay_key_write", "delete", "desc", "describe", "distinct", "distinctrow"
                        , "double", "drop", "end", "else", "escape", "escaped", "enclosed", "enum", "explain", "exists", "fields", "file", "first", "float"
                        , "float4", "float8", "flush", "foreign", "from", "for", "full", "function", "global", "grant", "grants", "group", "having", "heap"
                        , "high_priority", "hour", "hour_minute", "hour_second", "hosts", "identified", "ignore", "in", "index", "infile", "inner", "insert"
                        , "insert_id", "int", "integer", "interval", "int1", "int2", "int3", "int4", "int8", "into", "if", "is", "isam", "join", "key", "keys"
                        , "kill", "last_insert_id", "leading", "left", "length", "like", "lines", "limit", "load", "local", "lock", "logs", "long", "longblob"
                        , "longtext", "low_priority", "max", "max_rows", "match", "mediumblob", "mediumtext", "mediumint", "middleint", "min_rows", "minute"
                        , "minute_second", "modify", "month", "monthname", "myisam", "natural", "numeric", "no", "not", "null", "on", "optimize", "option"
                        , "optionally", "or", "order", "outer", "outfile", "pack_keys", "partial", "password", "precision", "primary", "procedure", "process"
                        , "processlist", "privileges", "read", "real", "references", "reload", "regexp", "rename", "replace", "restrict", "returns"
                        , "revoke", "rlike", "row", "rows", "second", "select", "set", "show", "shutdown", "smallint", "soname", "sql_big_tables"
                        , "sql_big_selects", "sql_low_priority_updates", "sql_log_off", "sql_log_update", "sql_select_limit", "sql_small_result"
                        , "sql_big_result", "sql_warnings", "straight_join", "starting", "status", "string", "table", "tables", "temporary", "terminated"
                        , "text", "then", "time", "timestamp", "tinyblob", "tinytext", "tinyint", "trailing", "to", "type", "use", "using", "unique"
                        , "unlock", "unsigned", "update", "usage", "values", "varchar", "variables", "varying", "varbinary", "with", "write", "when"
                        , "where", "year", "year_month", "zerofill");
                // 字段名
                String fieldName = Optional.ofNullable(row.getCell(0))
                        .map(Cell::getStringCellValue)
                        .map(e -> StrUtil.toUnderlineCase(e))
                        .filter(e -> !CollUtil.contains(strings, e))
                        .orElse("");
                // 字段注释
                String fieldComment = Optional.ofNullable(row.getCell(5))
                        .map(Cell::getStringCellValue)
                        .orElse("");
                // 追加注释
                String fieldCommentAppend = Optional.ofNullable(row.getCell(6))
                        .map(Cell::getStringCellValue)
                        .orElse("");
                //合体注释
                String syndicationNotes =
                        isBlank(fieldCommentAppend)
                                ? fieldComment
                                : StrUtil.concat(false, fieldComment, ":", fieldCommentAppend);

                if (isBlank(fieldName) && isBlank(fieldComment)) {
                    break;
                } else if (StrUtil.isNotBlank(fieldComment) && isBlank(fieldName)) {
                    fieldName = TransUtil.chinese2English(fieldComment);
                    fieldName = shortEng(fieldName, fieldComment);
                }
                // 字段类型
                String fieldType = row.getCell(1).getStringCellValue();
                // 字段长度
                Cell 字段长度 = row.getCell(2);
                字段长度.setCellType(CellType.STRING);
                String fieldLength = Optional.ofNullable(字段长度.getStringCellValue()).filter(StrUtil::isNotBlank).orElse("0");
                // 是否为主键
                Cell cell4 = row.getCell(3);
                // 是否自增
                Cell cell5 = row.getCell(4);


                String isPrimaryKey = Optional.ofNullable(cell4)
                        .map(Cell::getStringCellValue)
                        .filter(StrUtil::isNotBlank).orElse("");
                String isSelfIncreasing = Optional.ofNullable(cell5)
                        .map(Cell::getStringCellValue)
                        .filter(StrUtil::isNotBlank).orElse("");


                RangeContext rangeContext = new RangeContext();
                rangeContext.setFieldName(fieldName);
                rangeContext.setFieldComment(fieldComment);
                rangeContext.setFieldCommentAppend(fieldCommentAppend);
                rangeContext.setSyndicationNotes(syndicationNotes);
                rangeContext.setFieldType(fieldType);
                rangeContext.setFieldLength(fieldLength);
                rangeContext.setIsPrimaryKey(isPrimaryKey);
                rangeContext.setIsSelfIncreasing(isSelfIncreasing);

                list.add(rangeContext);

                ddlContext.setDto(list);

            }
            ddlContexts.add(ddlContext);

        }
        return ddlContexts;
    }

    private static String shortEng(String tableEnglishName, String tableChineseName) {
        if (StrUtil.length(tableEnglishName) > 15) {
            tableEnglishName = PinyinUtil.getFirstLetter(tableChineseName, "");
        }
        tableEnglishName = StrUtil.removeAny(tableEnglishName, "(", ")");

        tableEnglishName = tableEnglishName.replaceAll("\\((.*?)\\)", ""); // 移除括号及其内容
        tableEnglishName = tableEnglishName.replaceAll("(_{2,})", "_"); // 移除连续的下划线
        return tableEnglishName;
    }

    /**
     * 读取excel文件内容生成数据库表ddl
     */
    @SneakyThrows
    public static Workbook getWorkBookFromPath(String filePath) {
        if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
            System.out.println("文件不是excel类型");
        }
        BufferedInputStream inputStream = FileUtil.getInputStream(filePath);

        return filePath.endsWith(".xls")
                ? new HSSFWorkbook(inputStream)
                : new XSSFWorkbook(inputStream);

    }

    public static String removeNotChinese(String str) {
        if (StrUtil.isBlank(str)) {
            return "";
        }

        String regex = "[^\u4E00-\u9FA5]";
        String s1 = str.replaceAll(regex, "");
        return s1;
    }
}
