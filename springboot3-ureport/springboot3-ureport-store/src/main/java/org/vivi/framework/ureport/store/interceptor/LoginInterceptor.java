package org.vivi.framework.ureport.store.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestAccept = request.getHeader("accept");
        String contentType = "text/html";

        //判断请求类型
        if (StringUtils.isNotEmpty(requestAccept)) {

            if (StringUtils.contains(requestAccept, "application/json") || StringUtils.contains(requestAccept, "text/javascript")

                    || StringUtils.contains(requestAccept, "text/json")) {

                contentType = "application/json";

            }

        }

        if (contentType.equals("text/html")) {
            // 需要返回页面
            if(StpUtil.isLogin()){
                return true;
            }else{
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        } else if (contentType.equals("application/json")) {
            // 需要返回json
            if(!StpUtil.isLogin()) {

                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Cache-Control", "no-cache");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");

                Map<String, Object> result = new HashMap<>();

                result.put("code", -1);
                result.put("msg", "未登录或者登录超时");

                response.getWriter().println(JSONUtil.toJsonStr(result));
                response.getWriter().flush();

                return false;
            }else{
                return true;
            }

        } else {

            // 无法判断是页面还是json，根据实际给个默认处理
            return true;
        }


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
