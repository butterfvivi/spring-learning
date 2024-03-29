package org.spring.oauth2.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@SuppressWarnings("deprecation")//忽略过时警告
@Configuration
public class ClientSecurityConfig {

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                //所有请求都需经过授权认证
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().authenticated())
//                //配置登录URL
//                .oauth2Login(
//                        oauth2Login ->
//                        oauth2Login.loginPage("/oauth2/authorization/myoauth2")
//                ).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                .and()
//                //使用默认客户端配置
//                .oauth2Client(withDefaults());
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain authorizationClientSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and().logout()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and().oauth2Client()
                .and().oauth2Login();

        return http.build();
    }

}
