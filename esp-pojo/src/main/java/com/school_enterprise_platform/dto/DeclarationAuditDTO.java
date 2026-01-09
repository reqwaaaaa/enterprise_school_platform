package com.school_enterprise_platform.dto;

import lombok.Data;

@Data
public class DeclarationAuditDTO {
    private Byte status;        // 审核结果：0待审核 1通过 2不通过 3候补
    private Long reviewerId;    // 审核人ID（当前登录管理员，从 BaseContext 获取）
    // 审核意见和时间后续可扩展表结构后再添加
}