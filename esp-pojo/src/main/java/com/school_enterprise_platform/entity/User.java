package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户基础信息表，存储平台所有用户的基本信息
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID，自增主键，系统唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名，登录用户名，唯一
     */
    @TableField("username")
    private String username;

    /**
     * 密码，加密存储（MD5+盐加密）
     */
    @TableField("password")
    private String password;

    /**
     * 邮箱，唯一邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号，可用于登录/找回密码
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户类型：1=求职者,2=学生,3=HR,4=辅导员,5=讲师,6=政府管理员
     */
    @TableField("user_role")
    private Byte userRole;

    /**
     * 所属学校ID，学生/讲师/辅导员使用
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 所属企业ID，HR/求职者使用
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 组织类型：1=学校,2=企业,3=政府
     */
    @TableField("organization_type")
    private Byte organizationType;

    /**
     * 注册时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 最后登录时间
     */
    @TableField("last_login")
    private LocalDateTime lastLogin;

    /**
     * 状态：0=禁用,1=启用
     */
    @TableField("status")
    private Byte status;

    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 备注，额外说明
     */
    @TableField("remark")
    private String remark;

    /**
     * 密码盐值
     */
    @TableField("salt")
    private String salt;
}
