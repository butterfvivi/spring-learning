package org.vivi.framework.securityjwt.jwt.filter;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vivi.framework.securityjwt.common.constant.CommonConstants;
import org.vivi.framework.securityjwt.common.exception.BaseException;
import org.vivi.framework.securityjwt.jwt.user.AccountUserDetailsService;
import org.vivi.framework.securityjwt.jwt.util.JwtUtil;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    private AccountUserDetailsService accountUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(JwtUtil.HEADER);

        if (StrUtil.isBlankOrUndefined(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtUtil.getClaimsByToken(token);
        if (claims == null) {
            throw new BaseException(CommonConstants.BAD_REQUEST, "token异常");
        }
        if (jwtUtil.isTokenExpired(claims.getExpiration())) {
            throw new BaseException(CommonConstants.BAD_REQUEST, "token已过期");
        }

        String username = claims.getSubject();

        // 构建UsernamePasswordAuthenticationToken，这里密码为null，是因为提供了正确的token，实现自动登录
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, accountUserDetailsService.getUserAuthority(username));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
