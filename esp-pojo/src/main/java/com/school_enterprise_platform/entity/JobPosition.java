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
 * 职位信息表，存储企业发布的招聘职位详情和要求
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("job_position")
public class JobPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职位ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 企业ID
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 职位名称
     */
    @TableField("title")
    private String title;

    /**
     * 职位类型：1全职 2实习 3兼职
     */
    @TableField("type")
    private Byte type;

    /**
     * 工作地点
     */
    @TableField("location")
    private String location;

    /**
     * 薪资范围
     */
    @TableField("salary")
    private String salary;

    /**
     * 岗位要求
     */
    @TableField("requirements")
    private String requirements;

    /**
     * 招聘人数
     */
    @TableField("headcount")
    private Integer headcount;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /**
     * 截止时间
     */
    @TableField("deadline")
    private LocalDateTime deadline;

    /**
     * 状态：0草稿 1发布 2已结束
     */
    @TableField("status")
    private Byte status;
}
