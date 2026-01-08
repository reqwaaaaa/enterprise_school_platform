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
 * 社会个人用户表，存储社会求职者的详细个人信息
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("social_user")
public class SocialUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 社会用户ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联user表
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 性别：1=男,2=女
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

    /**
     * 工作经验年限
     */
    @TableField("work_experience")
    private Integer workExperience;
}
