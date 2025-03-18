package com.haifan.common.security;


import com.haifan.common.core.domin.R;
import com.haifan.common.core.enums.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 请求方式不支持
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handlerRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                 HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return R.fail(ResultCode.ERROR);
    }

    /**
     * 拦截运行时异常
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public R<?> handlerRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return R.fail(ResultCode.ERROR);
    }

    /**
     * 系统异常
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R<?> handlerException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return R.fail(ResultCode.ERROR);
    }

}
