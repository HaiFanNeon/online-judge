package com.haifan.common.security.exceptionHandler;


import com.haifan.common.core.domin.R;
import com.haifan.common.core.enums.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpRequest;

@Slf4j
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
        log.error("请求地址'{}', 不支持'{}'请求",requestURI, e.getMethod());
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
        log.error("请求地址'{}', 发生运行时异常",requestURI, e);
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
        log.error("请求地址'{}', 发生异常",requestURI, e);
        return R.fail(ResultCode.ERROR);
    }

}
