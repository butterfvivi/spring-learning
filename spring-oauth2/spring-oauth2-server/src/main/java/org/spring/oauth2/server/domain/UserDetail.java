package org.spring.oauth2.server.domain;

import lombok.Data;
import org.spring.oauth2.server.domain.model.Authority;
import org.spring.oauth2.server.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDetail implements UserDetails {

    private final User user; // 用户信息
    private final List<Authority> authority; // 用户权限列表

    // 实现 UserDetails 接口中的方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority.stream() // 将权限列表转换为流
                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority())) // 将 Authority 对象转换为 GrantedAuthority 对象
                .collect(Collectors.toSet()); // 将转换后的对象集合转换为 Set 类型并返回
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

}
