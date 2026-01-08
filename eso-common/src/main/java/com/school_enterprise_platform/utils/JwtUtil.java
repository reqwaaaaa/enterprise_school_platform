package com.school_enterprise_platform.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 工具类
 * 完全兼容苍穹外卖项目原有拦截器调用方式（JwtUtil.parseJWT(secretKey, token)）
 * 同时保留实例化生成 Token 的能力，支持管理端配置
 */
@Component
public class JwtUtil {

    // 管理端配置（与 esp.jwt.admin-* 对应）
    @Value("${esp.jwt.admin-secret-key}")
    private String adminSecretKey;

    @Value("${esp.jwt.admin-ttl}")
    private Long adminTtlSeconds;

    private SecretKey adminKey;

    @PostConstruct
    public void init() {
        // 初始化管理端密钥（HMAC-SHA256 要求密钥长度 ≥ 32 字节）
        this.adminKey = Keys.hmacShaKeyFor(adminSecretKey.getBytes());
    }

    /**
     * 生成 JWT Token（管理端统一使用）
     *
     * @param username 用户名（或用户ID字符串）
     * @return Token 字符串
     */
    public String generateToken(String username) {
        long expirationMillis = adminTtlSeconds * 1000; // 秒 → 毫秒

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(adminKey)
                .compact();
    }

    /**
     * 静态解析方法 —— 与苍穹外卖项目拦截器完全一致的调用方式
     * 拦截器中直接调用：JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token)
     *
     * @param secretKey 密钥字符串（从配置读取）
     * @param token     JWT Token
     * @return Claims
     */
    public static Claims parseJWT(String secretKey, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 实例方法解析 Token（内部或其他地方使用）
     *
     * @param token JWT Token
     * @return Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(adminKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 验证 Token 是否有效（校验用户名 + 过期时间）
     */
    public boolean validateToken(String token, String username) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject().equals(username)
                    && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 仅验证 Token 签名和过期时间（不校验用户名）
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}