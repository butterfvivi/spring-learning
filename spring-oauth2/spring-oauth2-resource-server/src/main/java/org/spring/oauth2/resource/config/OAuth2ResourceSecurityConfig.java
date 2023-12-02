//package org.spring.oauth2.resource.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration
//@EnableWebSecurity
//public class OAuth2ResourceSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests().anyRequest().authenticated().and()
//                .oauth2ResourceServer().jwt();
//
//        return http.build();
//    }
//
//}
