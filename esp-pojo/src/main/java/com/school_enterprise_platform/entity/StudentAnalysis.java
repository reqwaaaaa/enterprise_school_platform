package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 学生就业与学习汇总统计表，用于辅导员查看班级分析
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("student_analysis")
@ApiModel(value = "StudentAnalysis对象", description = "学生就业与学习汇总统计表，用于辅导员查看班级分析")
public class StudentAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生ID，主键")
    @TableId("student_id")
    private Long studentId;

    @ApiModelProperty("总课程数")
    @TableField("total_courses")
    private Integer totalCourses;

    @ApiModelProperty("已完成课程数")
    @TableField("completed_courses")
    private Integer completedCourses;

    @ApiModelProperty("平均分数")
    @TableField("avg_score")
    private BigDecimal avgScore;

    @ApiModelProperty("职位申请数")
    @TableField("job_apply_count")
    private Integer jobApplyCount;

    @ApiModelProperty("成功职位数")
    @TableField("job_success_count")
    private Integer jobSuccessCount;

    @ApiModelProperty("最后更新时间")
    @TableField("last_update")
    private LocalDateTime lastUpdate;
}
