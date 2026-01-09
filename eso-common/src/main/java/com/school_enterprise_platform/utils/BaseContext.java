package com.school_enterprise_platform.utils;

/**
 * 基于 ThreadLocal 封装工具类，用户保存和获取当前登录用户 id
 * 在 JWT 拦截器校验成功后调用 BaseContext.setCurrentId(userId)
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }
}