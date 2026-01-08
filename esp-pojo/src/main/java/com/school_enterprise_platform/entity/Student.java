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
 * 在校学生信息表，存储学生的学籍信息和学业相关信息
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联user表
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 学号
     */
    @TableField("student_number")
    private String studentNumber;

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
     * 学校ID
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 专业
     */
    @TableField("major")
    private String major;

    /**
     * 班级
     */
    @TableField("class_name")
    private String className;

    /**
     * 入学年份
     */
    @TableField("enrollment_year")
    private Integer enrollmentYear;

    /**
     * 辅导员ID
     */
    @TableField("counselor_id")
    private Long counselorId;
}
