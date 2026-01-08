package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 申报表，存储企业或用户的补贴申报信息和审核流程
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("declaration")
public class Declaration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申报ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 企业ID
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 用户ID（学员）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 申报类型：1高技能培训 2技能大师工作室等
     */
    @TableField("type")
    private Byte type;

    /**
     * 申报金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 申报时间
     */
    @TableField("apply_time")
    private LocalDateTime applyTime;

    /**
     * 审核状态：0待审核 1通过 2不通过 3候补
     */
    @TableField("status")
    private Byte status;

    /**
     * 审核人ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;
}
