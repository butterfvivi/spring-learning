package org.vivi.framework.securityjwt.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_user")
public class User implements Serializable {

    private Integer id;

    private String name;

    private String userName;

    private String password;

    private String phone;

    private Integer gender;

    private Boolean enabled;

    private LocalDateTime lastLoginTime;
}
