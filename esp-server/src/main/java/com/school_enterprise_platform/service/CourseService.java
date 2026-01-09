package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school_enterprise_platform.result.PageResult;

public interface CourseService extends IService<Course> {
    PageResult searchPublicCourses(String keyword, Page<Course> page);

    Course getCourseDetail(Long courseId);
}