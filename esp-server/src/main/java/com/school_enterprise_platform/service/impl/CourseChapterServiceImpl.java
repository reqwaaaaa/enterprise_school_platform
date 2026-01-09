package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;  // 正确导入
import com.school_enterprise_platform.entity.CourseChapter;
import com.school_enterprise_platform.mapper.CourseChapterMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.CourseChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CourseChapterServiceImpl extends ServiceImpl<CourseChapterMapper, CourseChapter> implements CourseChapterService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResult getCourseChapters(Long courseId, Page<CourseChapter> page) {
        String key = "seeker:course:chapter:" + courseId + ":page:" + page.getCurrent();

        // 先查缓存
        @SuppressWarnings("unchecked")
        Page<CourseChapter> cached = (Page<CourseChapter>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        // 缓存未命中，查数据库
        LambdaQueryWrapper<CourseChapter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseChapter::getCourseId, courseId)
                .orderByAsc(CourseChapter::getSequence);

        Page<CourseChapter> result = this.page(page, wrapper);

        // 写入缓存，30分钟过期
        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(30));

        return new PageResult(result.getTotal(), result.getRecords());
    }
}