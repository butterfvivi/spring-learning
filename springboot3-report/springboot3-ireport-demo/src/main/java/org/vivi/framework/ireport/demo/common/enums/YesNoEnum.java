package org.vivi.framework.ireport.demo.common.enums;

public enum YesNoEnum {

    YES {
        public Integer getCode() {
            return 1;
        }

        public String getName() {
            return "YES";
        }
    },
    NO {
        public Integer getCode() {
            return 2;
        }

        public String getName() {
            return "NO";
        }
    };

    public abstract Integer getCode();

    public abstract String getName();
}
