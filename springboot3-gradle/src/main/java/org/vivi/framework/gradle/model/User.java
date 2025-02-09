package org.vivi.framework.gradle.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_user")
public class User {

    private Long id;

    private String userName;

    private String phone;
}
