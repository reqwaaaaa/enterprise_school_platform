package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 企业HR信息表，存储企业招聘负责人的职位和部门信息
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("hr")
public class Hr implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * HR ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联user表
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 企业ID
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 部门
     */
    @TableField("department")
    private String department;

    /**
     * 职位
     */
    @TableField("position")
    private String position;
}
