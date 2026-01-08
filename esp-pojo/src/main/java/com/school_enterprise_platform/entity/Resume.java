package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 简历信息表，存储用户的多版本简历内容和求职信息
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("resume")
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 简历ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（社会个人用户或学生）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 简历版本
     */
    @TableField("version")
    private Integer version;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 性别：1男 2女
     */
    @TableField("gender")
    private Byte gender;

    /**
     * 出生日期
     */
    @TableField("birth_date")
    private LocalDate birthDate;

    /**
     * 学历
     */
    @TableField("education")
    private String education;
}
