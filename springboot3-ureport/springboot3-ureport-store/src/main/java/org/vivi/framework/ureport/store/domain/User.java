package org.vivi.framework.ureport.store.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class User {
    private String userName;
    private String password;
}
