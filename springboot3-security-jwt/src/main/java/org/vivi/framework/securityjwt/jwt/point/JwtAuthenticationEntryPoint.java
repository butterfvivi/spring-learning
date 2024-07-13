package org.vivi.framework.securityjwt.jwt.point;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.vivi.framework.securityjwt.common.response.R;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        R result = R.failed("请先登录");

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
