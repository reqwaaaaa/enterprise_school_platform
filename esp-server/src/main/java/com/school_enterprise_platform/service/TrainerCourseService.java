package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.dto.TrainerCourseStatsDTO;
import com.school_enterprise_platform.entity.Course;
import com.school_enterprise_platform.entity.CourseChapter;
import com.school_enterprise_platform.result.PageResult;

public interface TrainerCourseService {

    PageResult getMyCourses(Long trainerId, Page<Course> page);

    void addCourse(Long trainerId, Course course);

    void updateCourse(Long trainerId, Course course);

    void deleteCourse(Long trainerId, Long courseId);

    Course getMyCourseDetail(Long trainerId, Long courseId);

    void addChapter(Long trainerId, CourseChapter chapter);

    void updateChapter(Long trainerId, CourseChapter chapter);

    void deleteChapter(Long trainerId, Long chapterId);

    TrainerCourseStatsDTO getCourseStats(Long trainerId, Long courseId);

    void validateCourseOwnership(Long trainerId, Long courseId);
}