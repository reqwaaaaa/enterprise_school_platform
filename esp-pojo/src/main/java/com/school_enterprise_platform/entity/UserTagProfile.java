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
 * 用户能力画像表，用于学生/求职者能力结构分析
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("user_tag_profile")
public class UserTagProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 标签ID
     */
    @TableField("tag_id")
    private Long tagId;

    /**
     * 熟练度：1-5级
     */
    @TableField("proficiency")
    private Byte proficiency;

    /**
     * 来源：1课程 2考试 3人工评估
     */
    @TableField("source")
    private Byte source;
}
