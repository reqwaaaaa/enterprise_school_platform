package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 岗位标签表，用于岗位能力匹配推荐
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("job_tag")
public class JobTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 职位ID
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 标签ID
     */
    @TableField("tag_id")
    private Long tagId;

    /**
     * 权重，默认1.0
     */
    @TableField("weight")
    private BigDecimal weight;
}
