package org.vivi.framework.report.simple.common.enums;

public enum Enabled  {

    YES(1),NO(0);

    private Integer value;

    Enabled(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }


    public Boolean exist(Integer value) {
        return this.getValue().equals(value);
    }
}