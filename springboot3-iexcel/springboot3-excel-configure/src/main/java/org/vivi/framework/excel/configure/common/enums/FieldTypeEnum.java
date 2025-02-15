package org.vivi.framework.excel.configure.common.enums;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 字段 类型枚举
 */
public enum FieldTypeEnum {

    SHORT_MIN(1,"short"),SHORT_MAX(2,"java.lang.Short"),

    INT(3,"int"),INTEGER(4,"java.lang.Integer"),

    String(5,"java.lang.String"),

    FLOAT_MIN(6,"float"),FLOAT_MAX(7,"java.lang.Float"),

    DOUBLE_MIN(8,"double"),DOUBLE_MAX(9,"java.lang.Double"),

    LONG_MIN(10,"long"),LONG_MAX(11,"java.lang.Long"),

    BOOLEAN_MIN(12,"boolean"),BOOLEAN_MAX(13,"java.lang.Boolean"),

    DATE(14,"java.util.Date");

    private FieldTypeEnum(int index, String typeName){
        this.index = index;
        this.typeName = typeName;
    }

    private int index;
    private String typeName;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    public static Map<String,Integer> getMap(){
        Map<String,Integer> map = new HashMap<>();

        for (FieldTypeEnum fieldTypeEnum : FieldTypeEnum.values()){
            map.put(fieldTypeEnum.getTypeName(),fieldTypeEnum.getIndex());
        }
        return map;
    }
    public static FieldTypeEnum getByTypeName(String typeName){
        for (FieldTypeEnum fieldTypeEnum : FieldTypeEnum.values()){
            if(fieldTypeEnum.getTypeName().equals(typeName)){
                return fieldTypeEnum;
            }
        }
        return null;
    }

    public static FieldTypeEnum getByIndex(int index){
        for (FieldTypeEnum fieldTypeEnum : FieldTypeEnum.values()){
            if(fieldTypeEnum.getIndex() == index){
                return fieldTypeEnum;
            }
        }
        return null;
    }

    public static Class<?> getType(int index){
        switch (index){
            case 3:
                return int.class;
            case 4:
                return Integer.class;
            case 5:
                return java.lang.String.class;

            case 14:
                return Date.class;

            case 10:
                return long.class;
            case 11:
                return Long.class;
            default:
                return null;
        }
    }
}
