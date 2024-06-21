package org.vivi.framework.easyexcelsimple.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private Long id;

    private String userCode;

    private String userName;

    private String mobile;

    private Integer gender;

    private LocalDateTime createTime;
}

