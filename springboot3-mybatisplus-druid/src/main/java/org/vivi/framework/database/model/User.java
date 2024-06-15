package org.vivi.framework.database.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "u_user")
public class User {

    private Long id;

    private String userCode;

    private String userName;

    private String mobile;
}
