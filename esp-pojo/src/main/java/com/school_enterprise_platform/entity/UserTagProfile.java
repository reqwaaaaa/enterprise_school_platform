package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户能力画像表，用于学生/求职者能力结构分析
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("user_tag_profile")
@ApiModel(value = "UserTagProfile对象", description = "用户能力画像表，用于学生/求职者能力结构分析")
public class UserTagProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("标签ID")
    @TableField("tag_id")
    private Long tagId;

    @ApiModelProperty("熟练度：1-5级")
    @TableField("proficiency")
    private Byte proficiency;

    @ApiModelProperty("来源：1课程 2考试 3人工评估")
    @TableField("source")
    private Byte source;
}
