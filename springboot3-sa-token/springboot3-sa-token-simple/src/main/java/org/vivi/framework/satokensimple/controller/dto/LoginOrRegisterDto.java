package org.vivi.framework.satokensimple.controller.dto;

import lombok.Data;

@Data
public class LoginOrRegisterDto {

    private String id;

    private String userName;

    private String password;

    private String phone;

    private String gender;
}
