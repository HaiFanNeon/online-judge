package com.haifan.common.security.service;

import cn.hutool.core.lang.UUID;
import com.haifan.common.core.constants.CacheConstants;
import com.haifan.common.core.constants.JwtConstants;
import com.haifan.common.redis.service.RedisService;
import com.haifan.common.core.domin.LoginUser;
import com.haifan.common.core.untils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TokenService {

    @Autowired
    private RedisService redisService;

    public String createToken(Long userId, String secret, Integer identity,String nickName) {
        String userKey = UUID.fastUUID().toString();
        // 根据userId生成用户token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.LOGIN_USER_ID, userId);
        claims.put(JwtConstants.LOGIN_USER_KEY, userKey);
        String token = JwtUtils.createToken(claims, secret);
        // 使用redis 来存储用户的敏感信息，用户的 identity 1 表示普通用户 2 表示管理员用户 LoginUser 对象来存储
        // key 必须保证唯一性， 便于维护 统一前缀: logintoken:userId(根据雪花算法生成的字段) / logintoken:糊涂工具包中的UUID
        // String userKey = UUID.fastUUID().toString(); 提到上面，加入到token中，根据token中的信息可以查到redis中存储的信息，以及是否过期
        String key = getTokenKey(userKey);
        LoginUser loginUser = new LoginUser().setIdentity(identity).setNickName(nickName);
        // 过期时间设置为 720分钟
        redisService.setCacheObject(key, loginUser, CacheConstants.EXP, TimeUnit.MINUTES);

        return token;
    }

    /**
     * 延长token有效时间
     * @param token
     * @param secret
     */
    public void extendToken(String token, String secret) {
       String userKey =  getUserKey(token, secret);
//        String userKey = JwtUtils.getUserKey(claims); // 获取jwt中的userKey
        String tokenKey = getTokenKey(userKey); // redis中存储的key

        Long expire = redisService.getExpire(tokenKey, TimeUnit.MINUTES);
        if (expire != null && expire < CacheConstants.REFRESH_TIME) {
            redisService.expire(tokenKey, CacheConstants.EXP, TimeUnit.MINUTES);
        }
    }


    // 获取redis中存储的key
    private String getTokenKey(String userKey) {
        return CacheConstants.LOGIN_TOKEN_KEY + userKey;
    }

    public Boolean deleteToken(String token, String secret) {
        String userKey = getUserKey(token, secret);
        if (userKey == null) {
            log.info("userKey == null");
            return false;
        }
        String tokenKey = getTokenKey(userKey);
        if (tokenKey == null) {
            log.info("tokenKey == null");
            return false;
        }
        return redisService.deleteObject(tokenKey);
    }

    private String getUserKey(String token, String secret) {
        Claims claims = null;
        try {
            claims = JwtUtils.parseToken(token, secret); // 从令牌中获取信息，解析payload中信息,存储唯一标识信息
            if (claims == null) {
                return null;
            }
        } catch (Exception e) {
            log.error("token: {}, 解析token出现异常，",token,e);
        }

        return JwtUtils.getUserKey(claims);
    }

    /**
     * 获取登录对象在redis中存储的key
     * @param token
     * @param secret
     * @return
     */
    public LoginUser getLoginUser(String token, String secret) {
        String userKey = getUserKey(token, secret);
        if (userKey == null) {
            return null;
        }
        return redisService.getCacheObject(getTokenKey(userKey), LoginUser.class);

    }


}
