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
 * 系统日志表，存储平台操作日志用于审计和监控
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("system_log")
public class SystemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户ID，可为空（系统操作）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 操作类型：如登录、添加课程、审批等
     */
    @TableField("action_type")
    private String actionType;

    /**
     * 操作对象
     */
    @TableField("target")
    private String target;

    /**
     * 操作详细信息
     */
    @TableField("details")
    private String details;

    /**
     * 操作时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
