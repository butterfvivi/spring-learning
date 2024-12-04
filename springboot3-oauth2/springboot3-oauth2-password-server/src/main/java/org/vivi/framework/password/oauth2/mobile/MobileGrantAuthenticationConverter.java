package org.vivi.framework.password.oauth2.mobile;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.vivi.framework.password.constant.Oauth2Constant;

import java.util.HashMap;
import java.util.Map;

public class MobileGrantAuthenticationConverter implements AuthenticationConverter {

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!Oauth2Constant.GRANT_TYPE_MOBILE.equals(grantType)){
            return null;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //从request中提取请求参数，然后存入MultiValueMap<String, String>
        MultiValueMap<String, String> parameters = getParameters(request);

        String smsCode = parameters.getFirst(Oauth2Constant.SMS_CODE);
        if (!StringUtils.hasText(smsCode) || parameters.get(Oauth2Constant.SMS_CODE).size() != 1){
            throw new OAuth2AuthenticationException("无效请求，短信验证码不能为空！");
        }

        //收集要传入PasswordGrantAuthenticationToken构造方法的参数，
        //该参数接下来在PasswordGrantAuthenticationProvider中使用
        Map<String, Object> additionalParameters = new HashMap<>();
        //遍历从request中提取的参数，排除掉grant_type、client_id、code等字段参数，其他参数收集到additionalParameters中
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        //返回自定义的MobileGrantAuthenticationToken对象
        return new MobileGrantAuthenticationToken(authentication, additionalParameters);
    }

    private static MultiValueMap<String,String>  getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    multiValueMap.add(key, value);
                }
            }
        });
        return multiValueMap;
    }
}
