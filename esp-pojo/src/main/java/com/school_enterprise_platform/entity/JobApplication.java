package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 职位申请表，存储用户投递简历的申请记录和状态
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("job_application")
public class JobApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申请ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 职位ID
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 简历ID
     */
    @TableField("resume_id")
    private Long resumeId;

    /**
     * 申请时间
     */
    @TableField("apply_time")
    private LocalDateTime applyTime;

    /**
     * 状态：0待处理 1已通过 2已拒绝
     */
    @TableField("status")
    private Byte status;
}
