package cn.molu.generator.pojo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 陌路
 * @apiNote 数据库表细信息
 * @date 2024/1/17 12:49
 * @tool Created by IntelliJ IDEA
 */
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ColumnDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = 9196390045041955368L;
//    ====================查询所有表信息====================
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表名注释
     */
    private String tableComment;
    /**
     * 表数据引擎
     */
    private String engine;
    /**
     * 表字符集
     */
    private String tableCollation;
    /**
     * 表中数据条数
     */
    private String tableRows;
    /**
     * 表创建时间
     */
    private String createTime;

//        ====================查询指定表中的字段信息====================
    /**
     * 数据库名
     */
    private String tableSchema;
    /**
     * 字段名
     */
    private String columnName;
    /**
     * 默认值
     */
    private String columnDefault;
    /**
     * 是否可为空：YES、NO
     */
    private String isNullable;
    /**
     * 数据类型：int、varchar...
     */
    private String dataType;
    /**
     * 字段类型：int、varchar(30)...
     */
    private String columnType;
    /**
     * 是否主键：PRI
     */
    private String columnKey;
    /**
     * 是否自增：auto_increment（自增）
     */
    private String extra;
    /**
     * 字段注释
     */
    private String columnComment;

    /**
     * 是否主键
     *
     * @param columnKey 字段键
     * @return 是否主键
     */
    public boolean isPrimaryKey(String columnKey) {
        return StrUtil.equalsIgnoreCase(columnKey, "PRI");
    }

    /**
     * 获取主键字段名
     *
     * @param list 字段列表
     * @return 主键字段名
     */
    public String getPrimaryKey(List<ColumnDetail> list) {
        return Optional.ofNullable(list)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(item -> isPrimaryKey(item.columnKey))
                .findFirst()
                .orElseGet(ColumnDetail::new).columnName;
    }

    /**
     * 列表转Map
     *
     * @param list          列表
     * @param replacePrefix 替换前缀
     * @return Map
     */
    public Map<String, Object> listToMap(List<ColumnDetail> list, String replacePrefix) {
        if (Objects.isNull(list) || list.isEmpty()) return null;
        Map<String, Object> map = new HashMap<>(2);
        ColumnDetail detailVo = list.get(0);
        String voTableName = detailVo.getTableName().toLowerCase();
        String subTableName = voTableName.replace(replacePrefix, "");
        String className = toUpperFirst(subTableName);
        map.put("tableName", voTableName);
        map.put("tableComment", detailVo.getTableComment());
        map.put("className", StrUtil.upperFirst(className));
        map.put("subClassName", className);
        map.put("path", voTableName.replace("_", "/"));
        map.put("prem", voTableName.replace("_", ":"));
        map.put("currTime", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN));
        AtomicReference<String> pk = new AtomicReference<>();
        AtomicReference<String> pkType = new AtomicReference<>();
        AtomicReference<String> getterPk = new AtomicReference<>();
        AtomicReference<String> pkColumn = new AtomicReference<>();
        List<Map<String, String>> fields = new ArrayList<>(2);
        List<Map<String, String>> otherColumn = new ArrayList<>(2);
        list.forEach(vo -> {
            Map<String, String> field = new HashMap<>(2);
            field.put("columnName", vo.getColumnName());
            field.put("javaField", toUpperFirst(vo.getColumnName()));
            field.put("columnComment", vo.getColumnComment());
            String javaType = getJavaType(vo.getDataType());
            field.put("javaType", javaType);
            field.put("jdbcType", vo.getDataType().toUpperCase());
            field.put("jdbcTypeXml", getJdbcTypeXml(vo.getDataType().toLowerCase()));
            field.put("type", getType(vo.getDataType()));
            field.put("getter", StrUtil.upperFirst(toUpperFirst(vo.getColumnName())));
            if (isPrimaryKey(vo.getColumnKey())) {
                if (StrUtil.equalsIgnoreCase(javaType, "Integer")) {
                    javaType = "Long";
                }
                field.put("javaType", javaType);
                field.put("pkColumn", vo.getColumnName());
                pk.set(toUpperFirst(vo.getColumnName()));
                pkColumn.set(vo.getColumnName());
                pkType.set(javaType);
                getterPk.set(StrUtil.upperFirst(toUpperFirst(vo.getColumnName())));
            } else {
                otherColumn.add(field);
            }
            fields.add(field);
        });
        map.put("columns", fields);
        map.put("otherColumn", otherColumn);
        map.put("pk", pk);
        map.put("pkColumn", pkColumn);
        map.put("pkType", pkType);
        map.put("getterPk", getterPk);
        return map;
    }

    /**
     * @title 获取字段类型
     * @author 陌路
     * @date 2023/12/31 21:01
     */
    private String getJdbcTypeXml(String dataType) {
        return switch (dataType) {
            case "int", "tinyint" -> "INTEGER";
            case "char" -> "CHAR";
            case "mediumint" -> "MEDIUMINT";
            case "bigint" -> "BIGINT";
            case "float" -> "FLOAT";
            case "double" -> "DOUBLE";
            case "bit" -> "BIT";
            case "datetime", "date", "time", "timestamp" -> "TIMESTAMP";
            default -> "VARCHAR";
        };
    }

    /**
     * @title 获取字段类型
     * @author 陌路
     * @date 2023/12/31 21:01
     */
    private String getType(String dataType) {
        String javaType = getJavaType(dataType);
        String type = "java.lang." + dataType;
        if (StrUtil.equals(javaType, "Date")) {
            type = "java.util.Date";
        }
        return type;
    }

    /**
     * @title 获取字段类型
     * @author 陌路
     * @date 2023/12/31 21:01
     */
    private String getJavaType(String dataType) {
        return switch (dataType) {
            case "bigint" -> "Long";
            case "datetime", "date", "time", "timestamp" -> "Date";
            case "decimal", "double" -> "Double";
            case "float" -> "Float";
            case "int", "tinyint", "integer" -> "Integer";
            default -> "String";
        };
    }

    /**
     * 首字母转大写
     *
     * @param field 字段
     * @return 首字母大写
     */
    public String toUpperFirst(String field) {
        // 表名转驼峰命名
        StringBuilder string = new StringBuilder();
        if (StrUtil.isNotEmpty(field) && field.contains("_")) {
            for (String str : field.split("_")) {
                string.append(StrUtil.upperFirst(str));
            }
        } else {
            string = new StringBuilder(StrUtil.upperFirst(field));
        }
        return StrUtil.lowerFirst(string.toString());
    }
}
