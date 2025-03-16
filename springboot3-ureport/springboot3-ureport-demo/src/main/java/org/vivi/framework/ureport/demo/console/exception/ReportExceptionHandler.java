package org.vivi.framework.ureport.demo.console.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vivi.framework.ureport.demo.console.common.R;
import org.vivi.framework.ureport.demo.core.exception.ReportException;

@ControllerAdvice
public class ReportExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ReportException.class)
    public R globalException(HttpServletResponse response, ReportException ex) {

        String message = ex.getMessage();
        return R.error(message);
    }

}
