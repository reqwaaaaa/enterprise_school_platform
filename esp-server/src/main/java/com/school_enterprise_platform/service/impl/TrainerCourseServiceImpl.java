package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.dto.TrainerCourseStatsDTO;
import com.school_enterprise_platform.entity.Course;
import com.school_enterprise_platform.entity.CourseChapter;
import com.school_enterprise_platform.entity.CourseEnrollment;
import com.school_enterprise_platform.entity.LearningRecord;
import com.school_enterprise_platform.mapper.CourseMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TrainerCourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements TrainerCourseService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CourseChapterService courseChapterService;

    @Autowired
    private CourseEnrollmentService courseEnrollmentService;

    @Autowired
    private LearningRecordService learningRecordService;

    @Override
    public PageResult getMyCourses(Long trainerId, Page<Course> page) {
        String key = "trainer:course:list:" + trainerId + ":page:" + page.getCurrent();

        @SuppressWarnings("unchecked")
        Page<Course> cached = (Page<Course>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getTrainerId, trainerId)
                .orderByDesc(Course::getStartTime);  // 使用数据库实际存在的 start_time 字段排序

        Page<Course> result = this.page(page, wrapper);

        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(15));

        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Override
    public void addCourse(Long trainerId, Course course) {
        course.setTrainerId(trainerId);
        course.setStatus((byte) 0);  // 默认未发布
        this.save(course);

        // 失效我的课程列表缓存
        redisTemplate.delete("trainer:course:list:" + trainerId + ":*");
    }

    @Override
    public void updateCourse(Long trainerId, Course course) {
        validateCourseOwnership(trainerId, course.getId());
        this.updateById(course);

        redisTemplate.delete("trainer:course:list:" + trainerId + ":*");
    }

    @Override
    public void deleteCourse(Long trainerId, Long courseId) {
        validateCourseOwnership(trainerId, courseId);
        this.removeById(courseId);

        redisTemplate.delete("trainer:course:list:" + trainerId + ":*");
    }

    @Override
    public Course getMyCourseDetail(Long trainerId, Long courseId) {
        validateCourseOwnership(trainerId, courseId);
        return this.getById(courseId);
    }

    @Override
    public void addChapter(Long trainerId, CourseChapter chapter) {
        validateCourseOwnership(trainerId, chapter.getCourseId());
        courseChapterService.save(chapter);
    }

    @Override
    public void updateChapter(Long trainerId, CourseChapter chapter) {
        CourseChapter existing = courseChapterService.getById(chapter.getId());
        if (existing == null) {
            throw new RuntimeException("章节不存在");
        }
        validateCourseOwnership(trainerId, existing.getCourseId());
        courseChapterService.updateById(chapter);
    }

    @Override
    public void deleteChapter(Long trainerId, Long chapterId) {
        CourseChapter chapter = courseChapterService.getById(chapterId);
        if (chapter == null) {
            throw new RuntimeException("章节不存在");
        }
        validateCourseOwnership(trainerId, chapter.getCourseId());
        courseChapterService.removeById(chapterId);
    }

    @Override
    public TrainerCourseStatsDTO getCourseStats(Long trainerId, Long courseId) {
        validateCourseOwnership(trainerId, courseId);

        TrainerCourseStatsDTO stats = new TrainerCourseStatsDTO();

        // 报名人数
        long enrollmentCount = courseEnrollmentService.lambdaQuery()
                .eq(CourseEnrollment::getCourseId, courseId)
                .count();
        stats.setEnrollmentCount(enrollmentCount);

        // 已完成人数（status = 2 表示已完成）
        long completedCount = learningRecordService.lambdaQuery()
                .eq(LearningRecord::getCourseId, courseId)
                .eq(LearningRecord::getStatus, (byte) 2)
                .count();
        stats.setCompletedCount(completedCount);

        // 可选：计算完成率
        if (enrollmentCount > 0) {
            stats.setCompletionRate((double) completedCount / enrollmentCount * 100);
        } else {
            stats.setCompletionRate(0.0);
        }

        return stats;
    }

    @Override
    public void validateCourseOwnership(Long trainerId, Long courseId) {
        Course course = this.getById(courseId);
        if (course == null || !course.getTrainerId().equals(trainerId)) {
            throw new RuntimeException("无权限操作该课程");
        }
    }
}