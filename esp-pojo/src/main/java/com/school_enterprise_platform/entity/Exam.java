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
 * 考试信息表，存储各类考试的基本信息和时间安排
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("exam")
@ApiModel(value = "Exam对象", description = "考试信息表，存储各类考试的基本信息和时间安排")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("考试ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("考试名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("考试类型：1课程测验 2认证考试 3公共测试")
    @TableField("type")
    private Byte type;

    @ApiModelProperty("课程ID")
    @TableField("course_id")
    private Long courseId;

    @ApiModelProperty("开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;

    @ApiModelProperty("状态：0未发布 1进行中 2已结束")
    @TableField("status")
    private Byte status;

    @ApiModelProperty("创建人ID（讲师）")
    @TableField("creator_id")
    private Long creatorId;
}
