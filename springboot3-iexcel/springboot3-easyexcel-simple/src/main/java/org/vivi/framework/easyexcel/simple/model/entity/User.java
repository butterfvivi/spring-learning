package org.vivi.framework.easyexcel.simple.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    private Long id;

    private String userCode;

    private String userName;

    private String mobile;

    private Integer gender;

    private LocalDateTime createTime;
}

