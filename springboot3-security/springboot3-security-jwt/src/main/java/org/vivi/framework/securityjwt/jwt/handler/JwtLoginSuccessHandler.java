package org.vivi.framework.securityjwt.jwt.handler;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.vivi.framework.securityjwt.common.response.R;
import org.vivi.framework.securityjwt.jwt.util.JwtTokenProvider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private JwtTokenProvider jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");

        // 生成token，并放置到请求头中
        String token = jwtUtil.generateToken(authentication.getName());
        response.setHeader(JwtTokenProvider.HEADER, token);

        R result = R.failed("Success Logout");

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
