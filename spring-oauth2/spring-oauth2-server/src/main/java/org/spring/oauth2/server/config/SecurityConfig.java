package org.spring.oauth2.server.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.core.GrantedAuthority;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import static org.springframework.security.config.Customizer.withDefaults;

@SuppressWarnings("deprecation")//忽略过时警告
@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)//指定执行优先级
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        return http
                //为 OAuth2 认证服务器添加 OIDC 支持
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(withDefaults())
                .and()
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                //当未经身份验证的用户尝试访问受保护的资源时，将用户重定向到Security的默认登录页面
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                //配置 OAuth2 资源服务器以使用 JWT 令牌进行身份验证
                .build();

    }

    @Bean
    @Order(2)
    //表单登录与身份验证的请求授权
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(withDefaults())
                //授权任何已认证的用户可以访问任何请求
                .authorizeHttpRequests(authorize ->authorize.anyRequest().authenticated())
                .build();

    }

    /**
     * 该 Bean 提供了一个存储在内存中的用户
     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        var user1 = User.withUsername("test")
//                .password("test")
//                .roles("user")
//                .build();
//        return new InMemoryUserDetailsManager(user1);
//    }

    /**
     *  以纯文本形式保存密码，实际开发应该实现 BCryptPasswordEncoder()
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
    /*
        向OAuth2认证服务器注册一个客户端应用程序进行授权
        该 Bean 提供了一个内存中的注册客户端存储，用于 OAuth2 认证服务器的客户端授权
        客户端应使用此处设置的值作为配置项
    */
//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        //生成随机UUID作为客户端唯一标识，避免多个客户端时ID冲突
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("simple-client-id") //设置授权客户端ID
//                .clientSecret("simple-client-secret") //设置客户端密钥
//                .scope(OidcScopes.OPENID)
////                .scope("message.read")
////                .scope("message.write")
////                .scope(OidcScopes.PROFILE)
////                .scope("user_info")
////                .scope("pull_requests")
//                /*
//                    设置客户端的重定向 URI，当用户授权后，OAuth2 认证服务器将重定向到该 URI
//                    由于OAuth2认证服务器的安全性设置，此处必须使用127.0.0.1
//                    使用localhost会导致拒绝重定向
//                 */
//                .redirectUri("http://127.0.0.1:2002/login/oauth2/code/myoauth2")
//                //设置客户端的身份验证方法，这里使用了基本身份验证方法
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                //设置客户端的授权类型，这里使用了授权码授权类型
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
////                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
////                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
////                .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
//                .build();
//
//        return new InMemoryRegisteredClientRepository(registeredClient);
//    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("simple-client-id")
                .clientSecret("simple-client-secret")
                .scope("read")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .redirectUri("http://127.0.0.1:2002/login/oauth2/code/myoauth2")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            Authentication principal = context.getPrincipal();
            if (context.getTokenType().getValue().equals("id_token")) {
                context.getClaims().claim("test", "Test Id Token");
            }
            if (context.getTokenType().getValue().equals("access_token")) {
                context.getClaims().claim("test", "Test Access Token");
                Set<String> authorities = principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                context.getClaims().claim("authorities", authorities)
                        .claim("user", principal.getName());
            }

        };
    }



    //该Bean用于配置OAuth2认证服务器，该例中我们无需配置
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    //解码Jwt令牌
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    //提供Jwt令牌
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    //固定写法，生成RSA密钥对
    public static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
    }

    static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}