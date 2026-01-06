package com.school_enterprise_platform.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // 修正：配置文件路径为 esp.jwt
    @Value("${esp.jwt.admin-secret-key}")
    private String secret; // 签名密钥

    @Value("${esp.jwt.admin-ttl}")
    private Long ttlSeconds;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成 JWT Token
     * @param username 用户名（通常为 user_id 或 username）
     * @return Token 字符串
     */
    public String generateToken(String username) {
        long expirationMillis = ttlSeconds * 1000; // 秒 → 毫秒

        return Jwts.builder()
                .subject(username) // 主题（通常存用户名或用户ID）
                .issuedAt(new Date()) // 签发时间
                .expiration(new Date(System.currentTimeMillis() + expirationMillis)) // 过期时间
                .signWith(key) // 使用密钥签名（HMAC-SHA）
                .compact();
    }

    /**
     * 解析 Token 并返回 Claims
     * @param token JWT Token
     * @return Claims 对象
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)                    // 验证签名密钥
                .build()
                .parseSignedClaims(token)           // 解析签名后的 Claims
                .getPayload();                      // 获取载荷
    }

    /**
     * 从 Token 中获取用户名
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 验证 Token 是否有效
     * @param token Token
     * @param username 期望的用户名
     * @return 是否有效
     */
    public boolean validateToken(String token, String username) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject().equals(username)
                    && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // 包括签名错误、过期、格式错误等
            return false;
        }
    }

    /**
     * 验证 Token 是否有效（不校验用户名，仅校验签名和过期时间）
     * @param token Token
     * @return 是否有效
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