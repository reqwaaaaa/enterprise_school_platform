package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.*;
import com.school_enterprise_platform.mapper.*;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.AdminSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 全站搜索服务实现类（管理员专用）
 * 支持搜索课程、职位、消息、简历等
 */
@Service
public class AdminSearchServiceImpl implements AdminSearchService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private JobPositionMapper jobPositionMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @Override
    public PageResult globalSearch(String keyword, String type, Page page) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new PageResult(0L, new ArrayList<>());
        }

        String safeKeyword = keyword.trim();
        String cacheKey = "admin:search:" + type + ":" + safeKeyword + ":page:" + page.getCurrent();

        // 先查 Redis 缓存
        @SuppressWarnings("unchecked")
        Page<Object> cached = (Page<Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        PageResult result = null;

        switch (type.toLowerCase()) {
            case "course":
                result = searchCourses(safeKeyword, page);
                break;

            case "job_position":
            case "job":
                result = searchJobs(safeKeyword, page);
                break;

            case "user_message":
            case "message":
                result = searchMessages(safeKeyword, page);
                break;

            case "resume":
                result = searchResumes(safeKeyword, page);
                break;

            default:
                // 不支持的类型，返回空
                return new PageResult(0L, new ArrayList<>());
        }

        // 写入缓存（缓存通用 PageResult）
        redisTemplate.opsForValue().set(cacheKey, result, Duration.ofMinutes(10));

        return result;
    }

    private PageResult searchCourses(String keyword, Page page) {
        // 使用具体泛型 Page<Course>
        Page<Course> coursePage = new Page<>(page.getCurrent(), page.getSize());

        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w
                .like(Course::getName, keyword)
                .or()
                .like(Course::getIntroduction, keyword)
                .or()
                .like(Course::getTags, keyword)
        ).eq(Course::getStatus, (byte) 1);  // 只搜已发布

        Page<Course> result = courseMapper.selectPage(coursePage, wrapper);

        return new PageResult(result.getTotal(), result.getRecords());
    }

    private PageResult searchJobs(String keyword, Page page) {
        Page<JobPosition> jobPage = new Page<>(page.getCurrent(), page.getSize());

        LambdaQueryWrapper<JobPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w
                .like(JobPosition::getTitle, keyword)
                .or()
                .like(JobPosition::getRequirements, keyword)
                .or()
                .like(JobPosition::getLocation, keyword)
        ).eq(JobPosition::getStatus, (byte) 1);  // 只搜已发布

        Page<JobPosition> result = jobPositionMapper.selectPage(jobPage, wrapper);

        return new PageResult(result.getTotal(), result.getRecords());
    }

    private PageResult searchMessages(String keyword, Page page) {
        Page<UserMessage> messagePage = new Page<>(page.getCurrent(), page.getSize());

        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(UserMessage::getContent, keyword)
                .orderByDesc(UserMessage::getSendTime);

        Page<UserMessage> result = messageMapper.selectPage(messagePage, wrapper);

        return new PageResult(result.getTotal(), result.getRecords());
    }

    private PageResult searchResumes(String keyword, Page page) {
        Page<Resume> resumePage = new Page<>(page.getCurrent(), page.getSize());

        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Resume::getRealName, keyword)
                .or()
                .like(Resume::getEducation, keyword);

        Page<Resume> result = resumeMapper.selectPage(resumePage, wrapper);

        return new PageResult(result.getTotal(), result.getRecords());
    }
}