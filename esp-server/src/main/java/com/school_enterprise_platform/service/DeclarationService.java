package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.dto.DeclarationAuditDTO;
import com.school_enterprise_platform.entity.Declaration;
import com.school_enterprise_platform.result.PageResult;

import java.util.Map;

public interface DeclarationService {

    /**
     * 获取补贴申报列表（支持按状态过滤）
     */
    PageResult getDeclarationList(Byte status, Page<Declaration> page);

    /**
     * 审核补贴申报
     */
    void auditDeclaration(Long id, DeclarationAuditDTO auditDTO);

    /**
     * 全平台申报统计
     */
    Map<String, Object> getPlatformDeclareStats();
}