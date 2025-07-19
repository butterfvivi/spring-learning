package org.vivi.framework.kafka.partition;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = Shape.OBJECT)
public enum City {

    beijing(1, "北京"),
    tianjing(2, "天津"),
    shanghai(3, "上海"),
    ;

    private static Map<String, City> nameMap = new HashMap<>();

    static {
        for (City city : City.values()) {
            nameMap.put(city.getName(), city);
        }
    }

    private final int code;
    private final String name;

    private City(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static int getPartition(String name) {
        return nameMap.getOrDefault(name, City.beijing).getCode();
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
