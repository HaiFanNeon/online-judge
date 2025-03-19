package com.haifan.common.security.exceptionHandler;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.haifan.common.core.domin.R;
import com.haifan.common.core.enums.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * 运行时业务异常
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public R<?> serviceException(ServiceException e,
                                 HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}', 发生业务异常",requestURI, e);
        return R.fail(ResultCode.ERROR);
    }
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        log.error(e.getMessage());
        String message = join(e.getAllErrors(),
                DefaultMessageSourceResolvable::getDefaultMessage, ", ");
        return R.fail(ResultCode.FAILED_PARAMS_VALIDATE.getCode(), message);
    }
    private <E> String join(Collection<E> collection, Function<E, String>
            function, CharSequence delimiter) {
        if (CollUtil.isEmpty(collection)) {
            return StrUtil.EMPTY;
        }
        return collection.stream()
                .map(function)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(delimiter));
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
