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
 * 学校信息表，存储合作院校的基本信息和资质
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("school")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学校ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学校名称
     */
    @TableField("name")
    private String name;

    /**
     * 学校类型：本科/职业/研究型等
     */
    @TableField("type")
    private String type;

    /**
     * 所在地：城市/地区
     */
    @TableField("location")
    private String location;

    /**
     * 学校简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 注册时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;
}
