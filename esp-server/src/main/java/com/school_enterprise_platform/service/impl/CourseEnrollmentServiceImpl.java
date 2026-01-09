package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.entity.CourseEnrollment;
import com.school_enterprise_platform.mapper.CourseEnrollmentMapper;
import com.school_enterprise_platform.service.CourseEnrollmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CourseEnrollmentServiceImpl extends ServiceImpl<CourseEnrollmentMapper, CourseEnrollment> implements CourseEnrollmentService {

    @Override
    public void enrollCourse(Long userId, Long courseId) {
        CourseEnrollment enrollment = new CourseEnrollment();
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrollmentTime(LocalDateTime.now());
        enrollment.setStatus((byte) 1);  // 1 已报名

        this.save(enrollment);
    }
}