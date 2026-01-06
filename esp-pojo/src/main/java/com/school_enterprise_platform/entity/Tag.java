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
 * 标签主表，用于课程和岗位标签管理
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("tag")
@ApiModel(value = "Tag对象", description = "标签主表，用于课程和岗位标签管理")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("标签名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("类别：1技能 2行业 3岗位方向 4能力维度")
    @TableField("category")
    private Byte category;

    @ApiModelProperty("标签描述")
    @TableField("description")
    private String description;
}
