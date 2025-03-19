package com.haifan.common.core.untils;

import com.haifan.common.core.constants.JwtConstants;
import io.jsonwebtoken.*;
import java.util.Map;

/**
 * JWT工具类 - 用于生成和解析JSON Web Tokens（基于HMAC-SHA512算法）
 * 注意：需引入jjwt依赖（如io.jsonwebtoken:jjwt-api）
 */
public class JwtUtils {

    /**
     * 生成JWT令牌（HMAC-SHA512算法）
     * @param claims   JWT声明（payload）数据，如用户ID、角色等（建议包含过期时间exp）
     * @param security HMAC-SHA512签名密钥（建议长度至少64字符的强密码）
     * @return 生成的JWT字符串
     *
     * @示例
     * Map<String,Object> claims = new HashMap<>();
     * claims.put("userId", 1001);
     * claims.put("exp", new Date(System.currentTimeMillis() + 3600000)); // 1小时后过期
     * String token = JwtUtils.createToken(claims, "your-64-char-strong-secret-key");
     *
     * @注意 密钥长度不足会导致安全漏洞，推荐使用KeyGenerator生成安全密钥
     */
    public static String createToken(Map<String, Object> claims, String security) {
        return Jwts.builder()
                .setClaims(claims) // 设置声明数据
                .signWith(SignatureAlgorithm.HS512, security) // HMAC-SHA512算法 + 密钥
                .compact(); // 生成紧凑型URL安全字符串
    }

    /**
     * 解析并验证JWT令牌
     * @param token    待解析的JWT字符串
     * @param security 验证签名使用的密钥（需与生成时一致）
     * @return 解析后的声明对象
     * @throws ExpiredJwtException   令牌过期
     * @throws SignatureException    签名验证失败（密钥错误或数据篡改）
     * @throws MalformedJwtException 令牌格式错误（非JWT结构）
     * @throws JwtException          其他JWT相关异常
     *
     * @示例
     * try {
     *     Claims claims = JwtUtils.parseToken(token, "your-secret-key");
     *     Integer userId = claims.get("userId", Integer.class);
     * } catch (ExpiredJwtException e) {
     *     // 处理过期逻辑
     * }
     *
     * @注意 必须捕获JwtException及其子类异常，建议结合全局异常处理器
     */
    public static Claims parseToken(String token, String security) {
        return Jwts.parser()
                .setSigningKey(security) // 设置HMAC验证密钥
                .parseClaimsJws(token)  // 解析并验证签名
                .getBody(); // 获取payload数据
    }

    public static String getUserKey(Claims claims) {
        return toStr(claims.get(JwtConstants.LOGIN_USER_KEY));
    }

    private static String toStr(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    public static String getUserId(Claims claims) {
        return toStr(claims.get(JwtConstants.LOGIN_USER_ID));
    }
}