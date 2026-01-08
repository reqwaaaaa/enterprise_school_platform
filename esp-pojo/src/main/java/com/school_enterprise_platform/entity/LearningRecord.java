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
 * 学习记录表，存储用户的学习进度和完成状态
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("learning_record")
public class LearningRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID，自增主键
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
     * 章节ID
     */
    @TableField("chapter_id")
    private Long chapterId;

    /**
     * 学习状态：0未开始 1学习中 2已完成
     */
    @TableField("status")
    private Byte status;

    /**
     * 开始学习时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    @TableField("finish_time")
    private LocalDateTime finishTime;
}
