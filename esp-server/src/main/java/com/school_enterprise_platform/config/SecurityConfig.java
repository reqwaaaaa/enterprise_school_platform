// 文件路径：esp-server/src/main/java/com/school_enterprise_platform/config/SecurityConfig.java

package com.school_enterprise_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 * 目的：关闭默认认证，配合自定义 JWT 拦截器使用
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 关闭 CSRF
                .authorizeHttpRequests(authz -> authz
                        // 放行登录、注册、文档等公共接口
                        .requestMatchers(
                                "/auth/login",
                                "/auth/register",
                                "/doc.html",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // 其他所有请求都需要认证（由 JWT 拦截器处理）
                        .anyRequest().authenticated()
                )
                // 关闭默认的表单登录和 Basic Auth
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}