package org.vivi.framework.securityjwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.vivi.framework.securityjwt.jwt.user.AccountUserDetailsService;
import org.vivi.framework.securityjwt.jwt.filter.JwtAuthenticationFilter;
import org.vivi.framework.securityjwt.jwt.handler.*;
import org.vivi.framework.securityjwt.jwt.point.JwtAuthenticationEntryPoint;

import static org.vivi.framework.securityjwt.common.constant.CommonConstants.WHITELIST;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AccountUserDetailsService accountUserDetailsService;

    @Autowired
    private JwtLoginSuccessHandler loginSuccessHandler;

    @Autowired
    private JwtLoginFailureHandler loginFailureHandler;

    @Autowired
    private JwtLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        //创建一个用户认证提供者
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //设置用户相关信息，可以从数据库中读取、或者缓存、或者配置文件
        authenticationProvider.setUserDetailsService(accountUserDetailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //禁用csrf
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.successHandler(loginSuccessHandler).failureHandler(loginFailureHandler))
                .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler))
                //使用无状态session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(WHITELIST).permitAll().anyRequest().authenticated())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler))
                //添加jwt过滤器
                .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
