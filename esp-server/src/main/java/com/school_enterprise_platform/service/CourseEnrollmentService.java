package com.school_enterprise_platform.service;

import com.school_enterprise_platform.entity.CourseEnrollment;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CourseEnrollmentService extends IService<CourseEnrollment> {
    void enrollCourse(Long userId, Long courseId);
}