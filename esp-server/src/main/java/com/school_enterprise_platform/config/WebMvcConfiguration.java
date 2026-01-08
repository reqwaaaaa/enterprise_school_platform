package com.school_enterprise_platform.config;

import com.school_enterprise_platform.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类，注册web层相关组件
 * 已适配 Spring Boot 3.3.5 + SpringDoc OpenAPI（替代废弃的 Swagger2/SpringFox）
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义 JWT 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义 JWT 拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/**")                          // 拦截所有路径
                .excludePathPatterns(
                        "/auth/login",                           // 登录接口放行
                        "/auth/register",                        // 注册接口放行（如有）
                        "/doc.html",                             // Knife4j/SpringDoc 页面
                        "/webjars/**",
                        "/v3/api-docs/**",                       // OpenAPI JSON
                        "/swagger-ui/**"                         // SpringDoc UI（如使用）
                );
    }

    /**
     * 设置静态资源映射（Knife4j / SpringDoc 页面所需）
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("设置静态资源映射...");
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}