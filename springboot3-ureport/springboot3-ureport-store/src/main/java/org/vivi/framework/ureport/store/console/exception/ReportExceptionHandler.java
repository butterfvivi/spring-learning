package org.vivi.framework.ureport.store.console.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.vivi.framework.ureport.store.console.common.R;
import org.vivi.framework.ureport.store.core.exception.ReportException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Author: summer
 * @Date: 2022/2/11 22:20
 * @Description:
 **/
@ControllerAdvice
public class ReportExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ReportException.class)
    public R globalException(HttpServletResponse response, ReportException ex){
        
        String message = ex.getMessage();
        return R.error(message);
    }

}
