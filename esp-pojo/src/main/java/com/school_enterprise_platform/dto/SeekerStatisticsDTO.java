package com.school_enterprise_platform.dto;

import lombok.Data;

/**
 * 求职者个人统计 DTO
 * 用于 /seeker/statistics 接口返回个人求职与学习统计数据
 *
 * 字段说明（可根据需求扩展）：
 * - jobApplyCount        : 职位投递次数
 * - courseEnrollmentCount: 课程报名次数
 * - completedCourses     : 已完成课程数
 * - learningHours        : 总学习时长（可选扩展）
 * - examPassCount        : 通过考试数（可选扩展）
 */
@Data
public class SeekerStatisticsDTO {

    /**
     * 职位投递次数
     */
    private Long jobApplyCount = 0L;

    /**
     * 课程报名次数
     */
    private Long courseEnrollmentCount = 0L;

    /**
     * 已完成课程数
     */
    private Long completedCourses = 0L;

    /**
     * 总学习时长（小时，可选）
     */
    private Integer learningHours = 0;

    /**
     * 通过考试数（可选）
     */
    private Long examPassCount = 0L;

    // 如需扩展其他统计指标，可继续添加字段
}