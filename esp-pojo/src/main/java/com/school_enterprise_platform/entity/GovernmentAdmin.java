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
 * 政府管理员表，存储政府工作人员的角色权限信息
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("government_admin")
public class GovernmentAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联user表
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 所属部门，如“郫都区人社局”
     */
    @TableField("department")
    private String department;

    /**
     * 角色权限：1审核员 2统计员 3系统运维等
     */
    @TableField("role")
    private String role;

    /**
     * 状态：0禁用 1启用
     */
    @TableField("status")
    private Byte status;
}
