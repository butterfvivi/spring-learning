package org.vivi.framework.dynamic.simple.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User {

    private Long id;

    private String userCode;

    private String userName;

    private String mobile;
}
