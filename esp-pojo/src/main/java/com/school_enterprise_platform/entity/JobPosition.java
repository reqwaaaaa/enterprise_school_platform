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
 * 职位信息表，存储企业发布的招聘职位详情和要求
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("job_position")
@ApiModel(value = "JobPosition对象", description = "职位信息表，存储企业发布的招聘职位详情和要求")
public class JobPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("职位ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("企业ID")
    @TableField("enterprise_id")
    private Long enterpriseId;

    @ApiModelProperty("职位名称")
    @TableField("title")
    private String title;

    @ApiModelProperty("职位类型：1全职 2实习 3兼职")
    @TableField("type")
    private Byte type;

    @ApiModelProperty("工作地点")
    @TableField("location")
    private String location;

    @ApiModelProperty("薪资范围")
    @TableField("salary")
    private String salary;

    @ApiModelProperty("岗位要求")
    @TableField("requirements")
    private String requirements;

    @ApiModelProperty("招聘人数")
    @TableField("headcount")
    private Integer headcount;

    @ApiModelProperty("发布时间")
    @TableField("publish_time")
    private LocalDateTime publishTime;

    @ApiModelProperty("截止时间")
    @TableField("deadline")
    private LocalDateTime deadline;

    @ApiModelProperty("状态：0草稿 1发布 2已结束")
    @TableField("status")
    private Byte status;
}
