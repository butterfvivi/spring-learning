package org.vivi.framework.satokensimple.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SatokenConfiguration implements WebMvcConfigurer {

    public static final String[] WHITE_LIST = {
            "/user/register",
            "/user/login",
            "/user/logout",
            "/swagger-ui/**",
            "/v3/**",
            "/files/{fileName}",
    };

    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    // 注册 Sa-Token 拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 登录拦截，放行白名单
                    SaRouter.match("/**")
                            .notMatch(WHITE_LIST)
                            .check(r -> StpUtil.checkLogin());

                }))
                .addPathPatterns("/**");
    }
}
