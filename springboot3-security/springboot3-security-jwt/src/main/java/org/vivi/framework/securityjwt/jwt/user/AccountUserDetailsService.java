package org.vivi.framework.securityjwt.jwt.user;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vivi.framework.securityjwt.model.entity.AccountUser;
import org.vivi.framework.securityjwt.model.entity.User;
import org.vivi.framework.securityjwt.service.UserService;

import java.util.List;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userInfo = userService.getUserInfo(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return new AccountUser(userInfo.getId(), userInfo.getUsername(), userInfo.getPassword(), getUserAuthority(userInfo.getUsername()));
    }

    public List<GrantedAuthority> getUserAuthority(String username) {

        String authority = "admin";

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
