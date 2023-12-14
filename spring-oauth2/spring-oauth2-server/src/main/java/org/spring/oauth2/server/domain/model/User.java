package org.spring.oauth2.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer id; // 用户 ID

    private String name; // 用户名

    private String password; // 密码

    // 使用 @Builder.Default 指定默认值
    @Builder.Default
    private Boolean accountNonExpired = true; // 账号是否未过期，默认为 true

    @Builder.Default
    private Boolean accountNonLocked = true; // 账号是否未锁定，默认为 true

    @Builder.Default
    private Boolean credentialsNonExpired = true; // 凭证是否未过期，默认为 true

    @Builder.Default
    private Boolean enabled = true; // 账号是否可用，默认为 true
}
