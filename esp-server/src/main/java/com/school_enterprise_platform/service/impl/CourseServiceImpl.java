package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;  // 正确导入
import com.school_enterprise_platform.entity.Course;
import com.school_enterprise_platform.entity.LearningRecord;
import com.school_enterprise_platform.mapper.CourseMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CourseEnrollmentService courseEnrollmentService;

    @Autowired
    private LearningRecordService learningRecordService;

    @Override
    public PageResult searchPublicCourses(String keyword, Page<Course> page) {
        // 构建缓存键，处理 keyword 为 null 的情况
        String safeKeyword = keyword == null ? "" : keyword;
        String key = "seeker:course:public:search:" + safeKeyword + ":page:" + page.getCurrent();

        // 先查缓存
        @SuppressWarnings("unchecked")
        Page<Course> cached = (Page<Course>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        // 缓存未命中，查数据库
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getType, (byte) 1)                    // 公共课程
                .eq(Course::getStatus, (byte) 1);                // 已发布

        if (keyword != null && !keyword.isBlank()) {
            wrapper.like(Course::getName, keyword);
        }

        wrapper.orderByDesc(Course::getStartTime);

        Page<Course> result = this.page(page, wrapper);

        // 写入缓存，10分钟过期
        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(10));

        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Override
    public Course getCourseDetail(Long courseId) {
        String key = "seeker:course:detail:" + courseId;

        @SuppressWarnings("unchecked")
        Course course = (Course) redisTemplate.opsForValue().get(key);
        if (course == null) {
            course = this.getById(courseId);
            if (course != null && course.getType() == (byte) 1 && course.getStatus() == (byte) 1) {
                redisTemplate.opsForValue().set(key, course, Duration.ofHours(1));
            } else {
                throw new RuntimeException("课程不存在或未发布");
            }
        }
        return course;
    }

    @Override
    public Map<String, Object> getPlatformTrainingStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalCourses", this.count());
        stats.put("activeCourses", this.lambdaQuery().eq(Course::getStatus, (byte) 1).count());
        stats.put("enrollmentCount", courseEnrollmentService.count());
        stats.put("completedCount", learningRecordService.lambdaQuery()
                .eq(LearningRecord::getStatus, (byte) 2)
                .count());

        return stats;
    }
}