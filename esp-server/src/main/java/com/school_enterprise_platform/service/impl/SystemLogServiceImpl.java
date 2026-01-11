package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;  // 关键正确导入！
import com.school_enterprise_platform.entity.SystemLog;
import com.school_enterprise_platform.mapper.SystemLogMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 系统日志服务实现类（管理员专用）
 * 支持日志分页查询 + Redis 缓存
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResult getLogs(Page<SystemLog> page, Long userId) {
        String key = "admin:logs:user:" + (userId == null ? "all" : userId) + ":page:" + page.getCurrent();

        // 先查 Redis 缓存
        @SuppressWarnings("unchecked")
        Page<SystemLog> cached = (Page<SystemLog>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(SystemLog::getUserId, userId);
        }
        wrapper.orderByDesc(SystemLog::getCreateTime);

        Page<SystemLog> result = this.page(page, wrapper);

        // 写入缓存，30分钟过期
        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(30));

        return new PageResult(result.getTotal(), result.getRecords());
    }
}