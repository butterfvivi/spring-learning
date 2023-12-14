//package org.spring.oauth2.client.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class Oauth2ClientAutoConfiguration {
//
//    @Bean
//    public SecurityFilterChain authorizationClientSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests()
//                .anyRequest().authenticated()
//                .and().logout()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                .and().oauth2Client()
//                .and().oauth2Login();
//
//        return http.build();
//    }
//
//    //    @Bean
////    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                //所有请求都需经过授权认证
////                .authorizeHttpRequests(authorize -> authorize
////                        .anyRequest().authenticated())
////                //配置登录URL
////                .oauth2Login(oauth2Login ->
////                        oauth2Login.loginPage("/oauth2/authorization/myoauth2"))
////                //使用默认客户端配置
////                .oauth2Client(withDefaults());
////        return http.build();
////    }
//}

