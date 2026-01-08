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
 * 培训讲师信息表，存储讲师的基本信息和教学资质
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("trainer")
public class Trainer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 讲师ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联user表
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 学校ID
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 职称
     */
    @TableField("title")
    private String title;
}
