package org.vivi.framework.password.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vivi.framework.password.domain.model.UserInfo;
import org.vivi.framework.password.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.getUserByName(username);
        List<SimpleGrantedAuthority> authorities = Arrays.asList("USER").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return Optional.ofNullable(userInfo)
                .map(u -> new User(username, userInfo.getPassword(),authorities))
                .orElseThrow(() -> new UsernameNotFoundException("UserInfo not found"));
    }

}
