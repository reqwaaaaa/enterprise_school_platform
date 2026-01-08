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
 * 课程报名表，存储用户报名课程的记录和状态
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("course_enrollment")
public class CourseEnrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报名ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 课程ID
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 报名时间
     */
    @TableField("enrollment_time")
    private LocalDateTime enrollmentTime;

    /**
     * 状态：0取消 1已报名 2完成
     */
    @TableField("status")
    private Byte status;
}
