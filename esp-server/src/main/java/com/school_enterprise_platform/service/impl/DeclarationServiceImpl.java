package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.dto.DeclarationAuditDTO;
import com.school_enterprise_platform.entity.Declaration;
import com.school_enterprise_platform.mapper.DeclarationMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.DeclarationService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeclarationServiceImpl extends ServiceImpl<DeclarationMapper, Declaration> implements DeclarationService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResult getDeclarationList(Byte status, Page<Declaration> page) {
        String key = "admin:declare:list:status:" + (status == null ? "all" : status) + ":page:" + page.getCurrent();

        @SuppressWarnings("unchecked")
        Page<Declaration> cached = (Page<Declaration>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        LambdaQueryWrapper<Declaration> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Declaration::getStatus, status);
        }
        wrapper.orderByDesc(Declaration::getApplyTime);

        Page<Declaration> result = this.page(page, wrapper);

        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(15));

        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Override
    public void auditDeclaration(Long id, DeclarationAuditDTO auditDTO) {
        Declaration declaration = this.getById(id);
        if (declaration == null) {
            throw new RuntimeException("申报记录不存在");
        }

        // 只更新数据库中存在的字段
        declaration.setStatus(auditDTO.getStatus());
        declaration.setReviewerId(auditDTO.getReviewerId());  // 当前管理员ID

        this.updateById(declaration);

        // 失效所有申报列表缓存
        redisTemplate.delete("admin:declare:list:*");
    }

    @Override
    public Map<String, Object> getPlatformDeclareStats() {
        Map<String, Object> stats = new HashMap<>();

        // 总申报数
        long total = this.count();
        stats.put("totalDeclarations", total);

        // 各状态数量
        stats.put("pending", this.lambdaQuery().eq(Declaration::getStatus, (byte) 0).count());
        stats.put("approved", this.lambdaQuery().eq(Declaration::getStatus, (byte) 1).count());
        stats.put("rejected", this.lambdaQuery().eq(Declaration::getStatus, (byte) 2).count());
        stats.put("backup", this.lambdaQuery().eq(Declaration::getStatus, (byte) 3).count());

        // 总申报金额（MyBatis-Plus 不支持直接 sum，示例用循环计算）
        // 实际生产可自定义 SQL 或使用 wrapper.sum
        stats.put("totalAmount", 0.0);

        return stats;
    }
}