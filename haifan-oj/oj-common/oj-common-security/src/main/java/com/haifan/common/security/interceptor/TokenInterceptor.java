package com.haifan.common.security.interceptor;


import cn.hutool.core.util.StrUtil;
import com.haifan.common.core.constants.HttpConstants;
import com.haifan.common.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret; // 从哪个配置文件中读取，取决于这个bean对象交给了哪个服务的spring容器进行管理

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = getToken(request);

        tokenService.extendToken(token, secret);
        return true;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(HttpConstants.AUTHENTICATION);
        if (!StrUtil.isEmpty(token) && token.startsWith(HttpConstants.PREFIX)) {
            token = token.replace(HttpConstants.PREFIX, StrUtil.EMPTY);
        }
        return token;
    }
}
