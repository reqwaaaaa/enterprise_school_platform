package com.school_enterprise_platform.service.impl;

import com.school_enterprise_platform.dto.SeekerStatisticsDTO;
import com.school_enterprise_platform.entity.CourseEnrollment;
import com.school_enterprise_platform.entity.JobApplication;
import com.school_enterprise_platform.entity.LearningRecord;
import com.school_enterprise_platform.service.CourseEnrollmentService;
import com.school_enterprise_platform.service.JobApplicationService;
import com.school_enterprise_platform.service.LearningRecordService;
import com.school_enterprise_platform.service.SeekerStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 求职者个人统计服务实现类
 * 统计：职位投递次数、课程报名次数、已完成课程数
 */
@Service
public class SeekerStatisticsServiceImpl implements SeekerStatisticsService {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private CourseEnrollmentService courseEnrollmentService;

    @Autowired
    private LearningRecordService learningRecordService;

    @Override
    public SeekerStatisticsDTO getStatistics(Long userId) {
        SeekerStatisticsDTO dto = new SeekerStatisticsDTO();

        // 职位投递次数
        dto.setJobApplyCount(
                jobApplicationService.lambdaQuery()
                        .eq(JobApplication::getUserId, userId)
                        .count()
        );

        // 课程报名次数
        dto.setCourseEnrollmentCount(
                courseEnrollmentService.lambdaQuery()
                        .eq(CourseEnrollment::getUserId, userId)
                        .count()
        );

        // 已完成课程数（status = 2 表示已完成，根据您的数据库设计）
        dto.setCompletedCourses(
                learningRecordService.lambdaQuery()
                        .eq(LearningRecord::getUserId, userId)
                        .eq(LearningRecord::getStatus, (byte) 2)
                        .count()
        );

        return dto;
    }
}