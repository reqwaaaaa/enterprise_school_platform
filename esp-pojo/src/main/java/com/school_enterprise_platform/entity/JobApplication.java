package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 职位申请表，存储用户投递简历的申请记录和状态
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("job_application")
@ApiModel(value = "JobApplication对象", description = "职位申请表，存储用户投递简历的申请记录和状态")
public class JobApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("申请ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("职位ID")
    @TableField("job_id")
    private Long jobId;

    @ApiModelProperty("简历ID")
    @TableField("resume_id")
    private Long resumeId;

    @ApiModelProperty("申请时间")
    @TableField("apply_time")
    private LocalDateTime applyTime;

    @ApiModelProperty("状态：0待处理 1已通过 2已拒绝")
    @TableField("status")
    private Byte status;
}
