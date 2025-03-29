package org.vivi.framework.ireport.demo.common.enums;

public enum SQLDriverEnum {

    MYSQL {
        public Integer getCode() {
            return 1;
        }

        public String getName() {
            return "com.mysql.cj.jdbc.Driver";
        }
    },

    ORACLE {
        public Integer getCode() {
            return 2;
        }

        public String getName() {
            return "oracle.jdbc.driver.OracleDriver";
        }
    };

    public abstract Integer getCode();

    public abstract String getName();


    public static SQLDriverEnum getByCode(Integer code) {
        for (SQLDriverEnum type : SQLDriverEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

}
