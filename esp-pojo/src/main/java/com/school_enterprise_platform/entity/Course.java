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
 * 课程信息表，存储平台所有课程的基本信息和教学资源
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程名称
     */
    @TableField("name")
    private String name;

    /**
     * 课程类型：1公共课程 2本校课程
     */
    @TableField("type")
    private Byte type;

    /**
     * 讲师ID
     */
    @TableField("trainer_id")
    private Long trainerId;

    /**
     * 学校ID（本校课程使用）
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 标签，多标签逗号分隔
     */
    @TableField("tags")
    private String tags;

    /**
     * 课程简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 课件文件路径，支持多个
     */
    @TableField("materials")
    private String materials;

    /**
     * 学习时长，单位：小时
     */
    @TableField("duration")
    private Integer duration;

    /**
     * 开课时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结课时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 状态：0未发布 1已发布 2已结束
     */
    @TableField("status")
    private Byte status;
}
