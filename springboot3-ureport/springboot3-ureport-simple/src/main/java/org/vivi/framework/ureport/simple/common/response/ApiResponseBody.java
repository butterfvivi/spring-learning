package org.vivi.framework.ureport.simple.common.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.vivi.framework.ureport.simple.common.bean.Result;


@RestControllerAdvice
public class ApiResponseBody implements ResponseBodyAdvice<Object>{

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		
		if(body instanceof Result){
			return body;
		}
		Result reuslt = new Result(body);
		reuslt.setMessage("success");
		if(body instanceof String){
			reuslt.setData(body);
			return reuslt.toString();
		}
		return reuslt;
	}

}
