package org.vivi.framework.satokensimple.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@TableName("t_user")
public class User {

    private String id;

    private String userName;

    private String password;

    private String phone;

    private String gender;

}
