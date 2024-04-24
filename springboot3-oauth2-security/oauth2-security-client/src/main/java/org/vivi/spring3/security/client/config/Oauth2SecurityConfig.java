package org.vivi.spring3.security.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class Oauth2SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 关闭 Basic认证
        http.httpBasic((basic)->basic.disable());
        // 关闭 CSRF
        http.csrf((csrf)->csrf.disable());
        // authorizeHttpRequests：指定多个授权规则，按照顺序
        http.authorizeHttpRequests((req)-> req.anyRequest().authenticated());
        // 表单登录
        http.formLogin(Customizer.withDefaults());
        // 开启 OAuth2 登录
        http.oauth2Login(Customizer.withDefaults());


        return http.build();
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .oauth2Login()
//                        .redirectionEndpoint()
//                                .and()
//                                        .userInfoEndpoint();
//        http.authorizeRequests()
//                // 允许地址
//                .requestMatchers("/oauth2/authorization", "/oauth2/code").permitAll()
//                // 所有地址验证
//                .and().authorizeRequests().anyRequest().authenticated().and().csrf().disable();
//
//        http.logout(l -> l
//                        .logoutSuccessUrl("/").permitAll()
//                );
//        return http.build();
//    }
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(this.giteeClientRegistration());
//    }
//
//    private ClientRegistration giteeClientRegistration() {
//        return ClientRegistration.withRegistrationId("gitee")
//                .clientId("fc0338b8fab5ec4faffb0f6d0ea57236229dcb9bd0eb2a5bbf8ce925b8d9612c")
//                .clientSecret("692c6c15d65e65c3be7c90d076bdb8df8bd2fcd25f6d217ba9e941ba051e6f8a")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .redirectUri("http://localhost:9000/oauth2/authorization/gitee")
//                .authorizationUri("https://gitee.com/oauth/authorize")
//                .tokenUri("https://gitee.com/oauth/token")
//                .userInfoUri("https://gitee.com/api/v5/user")
//                .userNameAttributeName("name")
//                .scope("user_info")
//                .build();
//    }

}
