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
 * 学习记录表，存储用户的学习进度和完成状态
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("learning_record")
@ApiModel(value = "LearningRecord对象", description = "学习记录表，存储用户的学习进度和完成状态")
public class LearningRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("课程ID")
    @TableField("course_id")
    private Long courseId;

    @ApiModelProperty("章节ID")
    @TableField("chapter_id")
    private Long chapterId;

    @ApiModelProperty("学习状态：0未开始 1学习中 2已完成")
    @TableField("status")
    private Byte status;

    @ApiModelProperty("开始学习时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @ApiModelProperty("完成时间")
    @TableField("finish_time")
    private LocalDateTime finishTime;
}
