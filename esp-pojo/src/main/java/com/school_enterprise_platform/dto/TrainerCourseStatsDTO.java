package com.school_enterprise_platform.dto;

import lombok.Data;

@Data
public class TrainerCourseStatsDTO {
    private Long enrollmentCount = 0L;  // 报名人数
    private Long completedCount = 0L;   // 完成人数
    private Double completionRate = 0.0; // 完成率（可后续计算）
}