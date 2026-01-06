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
 * 学校信息表，存储合作院校的基本信息和资质
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("school")
@ApiModel(value = "School对象", description = "学校信息表，存储合作院校的基本信息和资质")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("学校名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("学校类型：本科/职业/研究型等")
    @TableField("type")
    private String type;

    @ApiModelProperty("所在地：城市/地区")
    @TableField("location")
    private String location;

    @ApiModelProperty("学校简介")
    @TableField("introduction")
    private String introduction;

    @ApiModelProperty("注册时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("联系电话")
    @TableField("contact_phone")
    private String contactPhone;
}
