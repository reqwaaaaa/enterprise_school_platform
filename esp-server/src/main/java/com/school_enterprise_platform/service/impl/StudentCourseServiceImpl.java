package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.entity.Course;
import com.school_enterprise_platform.entity.User;
import com.school_enterprise_platform.mapper.CourseMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.StudentCourseService;
import com.school_enterprise_platform.service.UserService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class StudentCourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements StudentCourseService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserService userService;  // 关键注入

    @Override
    public PageResult searchMySchoolAndPublicCourses(Long userId, String keyword, Page<Course> page) {
        // 获取当前用户所属学校
        User user = userService.getById(userId);
        if (user == null || user.getSchoolId() == null) {
            throw new RuntimeException("用户不存在或未绑定学校");
        }
        Long schoolId = user.getSchoolId();

        String safeKeyword = keyword == null ? "" : keyword.trim();
        String key = "student:course:search:school:" + schoolId + ":keyword:" + safeKeyword + ":page:" + page.getCurrent();

        @SuppressWarnings("unchecked")
        Page<Course> cached = (Page<Course>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        // 公共课程 OR (本校课程 AND school_id 匹配)
        wrapper.and(w -> w.eq(Course::getType, (byte) 1)
                        .or()
                        .eq(Course::getType, (byte) 2).eq(Course::getSchoolId, schoolId))
                .eq(Course::getStatus, (byte) 1);  // 已发布

        if (!safeKeyword.isEmpty()) {
            wrapper.like(Course::getName, safeKeyword);
        }

        wrapper.orderByDesc(Course::getStartTime);

        Page<Course> result = this.page(page, wrapper);

        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(10));

        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Override
    public Course getCourseDetail(Long courseId) {
        String key = "student:course:detail:" + courseId;

        @SuppressWarnings("unchecked")
        Course course = (Course) redisTemplate.opsForValue().get(key);
        if (course == null) {
            course = this.getById(courseId);
            if (course == null || course.getStatus() != (byte) 1) {
                throw new RuntimeException("课程不存在或未发布");
            }

            // 权限校验：在校学生只能访问公共课程或本校课程
            Long currentUserId = BaseContext.getCurrentId();
            User user = userService.getById(currentUserId);
            if (user == null || user.getSchoolId() == null) {
                throw new RuntimeException("用户未绑定学校，无法访问课程");
            }

            if (course.getType() == (byte) 2 && !course.getSchoolId().equals(user.getSchoolId())) {
                throw new RuntimeException("无权限访问该课程");
            }

            redisTemplate.opsForValue().set(key, course, Duration.ofHours(1));
        }
        return course;
    }
}