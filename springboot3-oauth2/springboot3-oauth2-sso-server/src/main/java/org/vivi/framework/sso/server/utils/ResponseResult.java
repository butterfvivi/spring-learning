package org.vivi.framework.sso.server.utils;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.apache.commons.codec.Charsets;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.io.IOException;

@Data
public class ResponseResult<T> {

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 数据
     */
    private T data;

    private ResponseResult() {}


    /**
     *
     * @param body
     * @param resultCodeEnum
     * @return
     * @param <T>
     * @author  Rommel
     * @date    2023/7/31-10:46
     * @version 1.0
     * @description  构造返回结果
     */
    public static <T> ResponseResult<T> build(T body, ResultCodeEnum resultCodeEnum) {
        ResponseResult<T> result = new ResponseResult<>();
        //封装数据
        if(body != null) {
            result.setData(body);
        }
        //状态码
        result.setCode(resultCodeEnum.getCode());
        //返回信息
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }


    /**
     *
     * @return
     * @param <T>
     * @author  Rommel
     * @date    2023/7/31-10:45
     * @version 1.0
     * @description  成功-无参
     */
    public static<T> ResponseResult<T> ok() {
        return build(null,ResultCodeEnum.SUCCESS);
    }


    /**
     *
     * @param data
     * @return
     * @param <T>
     * @author  Rommel
     * @date    2023/7/31-10:45
     * @version 1.0
     * @description  成功-有参
     */
    public static<T> ResponseResult<T> ok(T data) {
        return build(data,ResultCodeEnum.SUCCESS);
    }

    /**
     *
     * @return
     * @param <T>
     * @author  Rommel
     * @date    2023/7/31-10:45
     * @version 1.0
     * @description  失败-无参
     */
    public static<T> ResponseResult<T> fail() {
        return build(null,ResultCodeEnum.FAIL);
    }

    /**
     *
     * @param data
     * @return
     * @param <T>
     * @author  Rommel
     * @date    2023/7/31-10:45
     * @version 1.0
     * @description  失败-有参
     */
    public static<T> ResponseResult<T> fail(T data) {
        return build(data,ResultCodeEnum.FAIL);
    }

    public ResponseResult<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public ResponseResult<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    /**
     *
     * @param response
     * @param e
     * @throws IOException
     * @author  Rommel
     * @date    2023/7/31-10:45
     * @version 1.0
     * @description  异常响应
     */
    public static void exceptionResponse(HttpServletResponse response, Exception e) throws AccessDeniedException, AuthenticationException,IOException {

        String message = null;
        if(e instanceof OAuth2AuthenticationException o){
            message = o.getError().getDescription();
        }else{
            message = e.getMessage();
        }
        exceptionResponse(response,message);
    }

    /**
     *
     * @param response
     * @param message
     * @throws AccessDeniedException
     * @throws AuthenticationException
     * @throws IOException
     * @author  Rommel
     * @date    2023/8/1-16:18
     * @version 1.0
     * @description  异常响应
     */
    public static void exceptionResponse(HttpServletResponse response,String message) throws AccessDeniedException, AuthenticationException,IOException {

        ResponseResult responseResult = ResponseResult.fail(message);
        Gson gson = new Gson();
        String jsonResult = gson.toJson(responseResult);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.getWriter().print(jsonResult);

    }

}