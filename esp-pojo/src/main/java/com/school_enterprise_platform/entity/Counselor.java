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
 * 辅导员信息表，存储高校辅导员的基本信息和所带班级信息
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("counselor")
@ApiModel(value = "Counselor对象", description = "辅导员信息表，存储高校辅导员的基本信息和所带班级信息")
public class Counselor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("辅导员ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("关联user表")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty("学校ID")
    @TableField("school_id")
    private Long schoolId;
}
