package com.haifan.common.security.service;

import cn.hutool.core.lang.UUID;
import com.haifan.common.core.constants.CacheConstants;
import com.haifan.common.core.constants.JwtConstants;
import com.haifan.common.redis.service.RedisService;
import com.haifan.common.security.domain.LoginUser;
import com.haifan.common.security.untils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TokenService {

    @Autowired
    private RedisService redisService;

    public String createToken(Long userId, String security, Integer identity) {
        String userKey = UUID.fastUUID().toString();
        // 根据userId生成用户token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.LOGIN_USER_ID, userId);
        claims.put(JwtConstants.LOGIN_USER_KEY, userKey);
        String token = JwtUtils.createToken(claims, security);
        // 使用redis 来存储用户的敏感信息，用户的 identity 1 表示普通用户 2 表示管理员用户 LoginUser 对象来存储
        // key 必须保证唯一性， 便于维护 统一前缀: logintoken:userId(根据雪花算法生成的字段) / logintoken:糊涂工具包中的UUID
        // String userKey = UUID.fastUUID().toString(); 提到上面，加入到token中，根据token中的信息可以查到redis中存储的信息，以及是否过期
        String key = CacheConstants.LOGIN_TOKEN_KEY + userKey;
        LoginUser loginUser = new LoginUser().setIndentity(identity);
        // 过期时间设置为 720分钟
        redisService.setCacheObject(key, loginUser, CacheConstants.EXP, TimeUnit.MINUTES);

        return token;
    }

}
