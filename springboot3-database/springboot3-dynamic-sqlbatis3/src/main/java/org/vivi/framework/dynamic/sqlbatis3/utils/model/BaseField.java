package org.vivi.framework.dynamic.sqlbatis3.utils.model;

public class BaseField {
    private String fieldName;
    private Class<?> type;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
