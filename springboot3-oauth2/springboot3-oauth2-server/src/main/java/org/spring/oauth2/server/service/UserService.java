package org.spring.oauth2.server.service;

import org.spring.oauth2.server.domain.UserDetail;
import org.spring.oauth2.server.domain.model.Authority;
import org.spring.oauth2.server.domain.model.User;
import org.spring.oauth2.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByName(username);
        List<Authority> authorities = userRepository.getAuthoritiesByUserId(user.getId());
        return Optional.ofNullable(user)
                .map(u -> new UserDetail(u, authorities))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
