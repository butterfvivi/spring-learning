package org.vivi.framework.sso.server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.Resource;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.vivi.framework.sso.server.constant.Oauth2Constant;
import org.vivi.framework.sso.server.filter.MyExceptionTranslationFilter;
import org.vivi.framework.sso.server.handler.MyAccessDeniedHandler;
import org.vivi.framework.sso.server.handler.MyAuthenticationEntryPoint;
import org.vivi.framework.sso.server.handler.MyAuthenticationFailureHandler;
import org.vivi.framework.sso.server.oauth2.mobile.MobileGrantAuthenticationConverter;
import org.vivi.framework.sso.server.oauth2.mobile.MobileGrantAuthenticationProvider;
import org.vivi.framework.sso.server.oauth2.oidc.IOidcUserInfoAuthenticationConverter;
import org.vivi.framework.sso.server.oauth2.oidc.IOidcUserInfoAuthenticationProvider;
import org.vivi.framework.sso.server.oauth2.oidc.IOidcUserInfoService;
import org.vivi.framework.sso.server.oauth2.password.PasswordGrantAuthenticationConverter;
import org.vivi.framework.sso.server.oauth2.password.PasswordGrantAuthenticationProvider;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class AuthorizationServerConfig {

    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private IOidcUserInfoService iOidcUserInfoService;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http, OAuth2AuthorizationService authorizationService,
                                                                      OAuth2TokenGenerator<?> tokenGenerator) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)

                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
                //设置自定义密码模式
                .tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint
                                .accessTokenRequestConverter(
                                        new PasswordGrantAuthenticationConverter())
                                .authenticationProvider(
                                        new PasswordGrantAuthenticationProvider(authorizationService, tokenGenerator)))
                //设置自定义手机验证码模式
                .tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint
                                .accessTokenRequestConverter(
                                        new MobileGrantAuthenticationConverter())
                                .authenticationProvider(
                                        new MobileGrantAuthenticationProvider(authorizationService, tokenGenerator)))
                .tokenEndpoint(tokenEndpoint ->{
                    tokenEndpoint.errorResponseHandler(new MyAuthenticationFailureHandler());
                })
                .clientAuthentication(clientAuthentication -> {
                    clientAuthentication.errorResponseHandler(new MyAuthenticationFailureHandler());
                })
                //开启OpenID Connect 1.0（其中oidc为OpenID Connect的缩写）。
                .oidc(oidcCustomizer -> {
                    oidcCustomizer.userInfoEndpoint(userInfoEndpointCustomizer -> {
                        userInfoEndpointCustomizer.userInfoRequestConverter(new IOidcUserInfoAuthenticationConverter(iOidcUserInfoService));
                        userInfoEndpointCustomizer.authenticationProvider(new IOidcUserInfoAuthenticationProvider(authorizationService,iOidcUserInfoService));
                    });
                });

        //设置登录地址，需要进行认证的请求被重定向到该地址
        http
                .addFilterBefore(new MyExceptionTranslationFilter(), ExceptionTranslationFilter.class)
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint(Oauth2Constant.LOGIN_URL),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                        .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                        .accessDeniedHandler(new MyAccessDeniedHandler())
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults()));

        return  http.build();
    }

    /**
     * 客户端信息
     * 对应表：oauth2_registered_client
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }


    /**
     * 授权信息
     * 对应表：oauth2_authorization
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 授权确认
     *对应表：oauth2_authorization_consent
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     *配置 JWK，为JWT(id_token)提供加密密钥，用于加密/解密或签名/验签
     * JWK详细见：https://datatracker.ietf.org/doc/html/draft-ietf-jose-json-web-key-41
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     *生成RSA密钥对，给上面jwkSource() 方法的提供密钥对
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * 配置jwt解析器
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     *配置认证服务器请求地址
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        //什么都不配置，则使用默认地址
        return AuthorizationServerSettings.builder().build();
    }

    /**
     *配置token生成器
     */
    @Bean
    OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtCustomizer(iOidcUserInfoService));
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }


//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
//        return context -> {
//            JwsHeader.Builder headers = context.getJwsHeader();
//            JwtClaimsSet.Builder claims = context.getClaims();
//            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
//                // Customize headers/claims for access_token
//
//            } else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
//                // Customize headers/claims for id_token
//                claims.claim(IdTokenClaimNames.AUTH_TIME, Date.from(Instant.now()));
//                StandardSessionIdGenerator standardSessionIdGenerator = new StandardSessionIdGenerator();
//                claims.claim("sid", standardSessionIdGenerator.generateSessionId());
//
//            }
//        };
//    }

    //包含客户端权限范围的，并没有包含用户的权限范围
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(IOidcUserInfoService iOidcUserInfoService) {

        return context -> {
            JwsHeader.Builder headers = context.getJwsHeader();
            JwtClaimsSet.Builder claims = context.getClaims();
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                // Customize headers/claims for access_token
                claims.claims(claimsConsumer->{
                    UserDetails userDetails = userDetailsService.loadUserByUsername(context.getPrincipal().getName());
                    claimsConsumer.merge("scope",userDetails.getAuthorities(),(scope,authorities)->{
                        Set<String> scopeSet = (Set<String>)scope;
                        Set<String> cloneSet = scopeSet.stream().map(String::new).collect(Collectors.toSet());
                        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = ( Collection<SimpleGrantedAuthority>)authorities;
                        simpleGrantedAuthorities.stream().forEach(simpleGrantedAuthority -> {
                            if(!cloneSet.contains(simpleGrantedAuthority.getAuthority())){
                                cloneSet.add(simpleGrantedAuthority.getAuthority());
                            }
                        });
                        return cloneSet;
                    });
                });

            } else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                // Customize headers/claims for id_token
                claims.claim(IdTokenClaimNames.AUTH_TIME, Date.from(Instant.now()));
                StandardSessionIdGenerator standardSessionIdGenerator = new StandardSessionIdGenerator();
                claims.claim("sid", standardSessionIdGenerator.generateSessionId());
            }
        };
    }

}
