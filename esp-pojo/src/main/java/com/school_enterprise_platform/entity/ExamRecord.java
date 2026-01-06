package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 考试记录表，存储用户参加考试的成绩和提交状态
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("exam_record")
@ApiModel(value = "ExamRecord对象", description = "考试记录表，存储用户参加考试的成绩和提交状态")
public class ExamRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("考试ID")
    @TableField("exam_id")
    private Long examId;

    @ApiModelProperty("得分")
    @TableField("score")
    private BigDecimal score;

    @ApiModelProperty("提交时间")
    @TableField("submit_time")
    private LocalDateTime submitTime;

    @ApiModelProperty("状态：0未提交 1已提交 2已批阅")
    @TableField("status")
    private Byte status;
}
