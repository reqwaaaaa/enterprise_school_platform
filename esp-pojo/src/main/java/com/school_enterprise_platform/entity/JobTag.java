package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 岗位标签表，用于岗位能力匹配推荐
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("job_tag")
@ApiModel(value = "JobTag对象", description = "岗位标签表，用于岗位能力匹配推荐")
public class JobTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("职位ID")
    @TableField("job_id")
    private Long jobId;

    @ApiModelProperty("标签ID")
    @TableField("tag_id")
    private Long tagId;

    @ApiModelProperty("权重，默认1.0")
    @TableField("weight")
    private BigDecimal weight;
}
