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
 * 企业信息表，存储合作企业的基本信息和认证状态
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("enterprise")
@ApiModel(value = "Enterprise对象", description = "企业信息表，存储合作企业的基本信息和认证状态")
public class Enterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("企业ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("企业名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("统一社会信用代码，唯一")
    @TableField("credit_code")
    private String creditCode;

    @ApiModelProperty("企业类型：如民企/国企")
    @TableField("type")
    private String type;

    @ApiModelProperty("所在地：城市/地区")
    @TableField("location")
    private String location;

    @ApiModelProperty("企业简介")
    @TableField("introduction")
    private String introduction;

    @ApiModelProperty("注册时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("联系电话")
    @TableField("contact_phone")
    private String contactPhone;
}
