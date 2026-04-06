package org.vivi.framework.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;

    private int sex;

    private String drlType;

    private int age;

    private String address;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
