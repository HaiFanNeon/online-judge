package com.haifan.gateway.filter;




import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.haifan.common.core.constants.CacheConstants;
import com.haifan.common.core.constants.HttpConstants;
import com.haifan.common.core.domin.LoginUser;
import com.haifan.common.core.domin.R;
import com.haifan.common.core.enums.ResultCode;
import com.haifan.common.core.enums.UserIdentity;
import com.haifan.common.redis.service.RedisService;
import com.haifan.common.core.untils.JwtUtils;
import com.haifan.gateway.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 网关鉴权过滤器 - 全局请求鉴权处理
 * 功能：JWT令牌验证、接口权限控制、登录状态检查
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private IgnoreWhiteProperties ignoreWhite;    // 白名单配置

    @Autowired
    private RedisService redisService;            // Redis操作服务

    @Value("${jwt.secret}")
    private String secret;                        // JWT加密密钥

    /**
     * 核心过滤逻辑（执行顺序通过getOrder()定义）
     * @param exchange 包含请求和响应的上下文对象
     * @param chain 网关过滤器链
     * @return Mono<Void>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();

        // 白名单路径直接放行（如登录接口）
        if (matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }

        // 从请求头获取令牌（需处理Bearer前缀）
        String token = getToken(request);
        if (StrUtil.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }

        Claims claims;
        try {
            // JWT令牌解析与签名验证
            claims = JwtUtils.parseToken(token, secret);
            if (claims == null) {
                return unauthorizedResponse(exchange, "令牌过期或验证不正确");
            }
        } catch (Exception e) {  // 捕获所有JWT解析异常
            log.error("令牌解析失败: {}", e.getMessage());
            return unauthorizedResponse(exchange, "令牌过期或验证不正确");
        }

        // 从JWT中获取用户唯一标识
        String userKey = JwtUtils.getUserKey(claims);
        String redisUserKey = getTokenKey(userKey);
        log.info(redisUserKey);
        if (!redisService.hasKey(redisUserKey)) {  // 检查Redis登录状态
            return unauthorizedResponse(exchange, "登录状态已过期");
        }

        // 验证JWT中的用户ID是否有效
        String userId = JwtUtils.getUserId(claims);
        if (StrUtil.isEmpty(userId)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        // 获取Redis中的登录用户信息
        LoginUser user = redisService.getCacheObject(redisUserKey, LoginUser.class);

        // 接口权限验证（根据URL前缀判断身份）
        if (url.contains(HttpConstants.SYSTEM_URL_PREFIX) &&  // 系统管理接口
                !UserIdentity.ADMIN.getValue().equals(user.getIndentity())) {  // 拼写错误：应为getIdentity()
            return unauthorizedResponse(exchange, "无管理员权限");
        }
        if (url.contains(HttpConstants.FRIEND_URL_PREFIX) &&   // 好友相关接口
                !UserIdentity.ORDINARY.getValue().equals(user.getIndentity())) {
            return unauthorizedResponse(exchange, "非普通用户权限");
        }

        // 所有验证通过，放行请求
        return chain.filter(exchange);
    }

    // -------------------------- 工具方法 --------------------------

    /**
     * 构建未授权响应（统一错误格式）
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常] 路径: {} | 原因: {}", exchange.getRequest().getPath(), msg);
        return webFluxResponseWriter(exchange.getResponse(), msg, ResultCode.FAILED_UNAUTHORIZED.getCode());
    }

    /**
     * WebFlux响应数据写入（JSON格式）
     * @param response 响应对象
     * @param msg 错误信息
     * @param code 错误码
     */
    private Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String msg, int code) {
        response.setStatusCode(HttpStatus.OK);  // 始终返回200状态码，错误信息在body中
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        R<?> result = R.fail(code, msg);
        byte[] bytes = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 过滤器执行顺序（值越小优先级越高）
     */
    @Override
    public int getOrder() {
        return -200;  // 高优先级，确保在其他过滤器前执行
    }

    /**
     * 路径匹配检查（是否在白名单中）
     * @param url 请求路径
     * @param ignoreWhite 白名单列表
     */
    public Boolean matches(String url, List<String> ignoreWhite) {
        if (StrUtil.isEmpty(url) || CollectionUtils.isEmpty(ignoreWhite)) {
            return false;
        }
        return ignoreWhite.stream().anyMatch(pattern -> isMatch(pattern, url));
    }

    /**
     * Ant风格路径匹配
     * 示例：
     * - /api/** 匹配所有/api开头的路径
     * - /user/* 匹配/user下的单级路径
     */
    public Boolean isMatch(String pattern, String url) {
        return new AntPathMatcher().match(pattern, url);
    }

    /**
     * 构建Redis存储的Token键名
     * 格式示例：login_tokens:user:123
     */
    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 从请求头提取Token（处理Bearer前缀）
     * 潜在问题：原代码中当token为空时调用startsWith会NPE
     */
    public String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HttpConstants.AUTHENTICATION);
        if (StrUtil.isNotEmpty(token) && token.startsWith(HttpConstants.PREFIX)) {
            return token.replace(HttpConstants.PREFIX, StrUtil.EMPTY);
        }
        return token;
    }
}
