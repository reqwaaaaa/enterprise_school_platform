package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.entity.JobPosition;
import com.school_enterprise_platform.mapper.JobPositionMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.JobPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class JobPositionServiceImpl extends ServiceImpl<JobPositionMapper, JobPosition> implements JobPositionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ============ 求职者：职位搜索 ============
    @Override
    public PageResult searchJobs(String keyword, String tags, Page<JobPosition> page) {
        String safeKeyword = keyword == null ? "" : keyword.trim();
        String safeTags = tags == null ? "" : tags.trim();
        String key = "seeker:job:search:kw:" + safeKeyword + ":tags:" + safeTags + ":page:" + page.getCurrent();

        @SuppressWarnings("unchecked")
        Page<JobPosition> cached = (Page<JobPosition>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        LambdaQueryWrapper<JobPosition> wrapper = new LambdaQueryWrapper<>();
        if (!safeKeyword.isEmpty()) {
            wrapper.like(JobPosition::getTitle, safeKeyword);
        }
        wrapper.eq(JobPosition::getStatus, (byte) 1)
                .orderByDesc(JobPosition::getPublishTime);

        // tags 过滤预留（后续可通过 job_tag 关联查询实现）
        // 当前简化：若有 tags，可扩展为 inSql 子查询

        Page<JobPosition> result = this.page(page, wrapper);
        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(10));

        return new PageResult(result.getTotal(), result.getRecords());
    }

    // ============ 求职者：职位详情 ============
    @Override
    public JobPosition getJobDetail(Long jobId) {
        String key = "seeker:job:detail:" + jobId;
        @SuppressWarnings("unchecked")
        JobPosition job = (JobPosition) redisTemplate.opsForValue().get(key);

        if (job == null) {
            job = this.getById(jobId);
            if (job != null && job.getStatus() == (byte) 1) {
                redisTemplate.opsForValue().set(key, job, Duration.ofHours(1));
            } else {
                throw new RuntimeException("职位不存在或已下架");
            }
        }
        return job;
    }

    // ============ HR：我的职位列表 ============
    public PageResult getMyJobs(Long enterpriseId, Page<JobPosition> page) {
        String key = "hr:job:list:enterprise:" + enterpriseId + ":page:" + page.getCurrent();

        @SuppressWarnings("unchecked")
        Page<JobPosition> cached = (Page<JobPosition>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        LambdaQueryWrapper<JobPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JobPosition::getEnterpriseId, enterpriseId)
                .orderByDesc(JobPosition::getPublishTime);

        Page<JobPosition> result = this.page(page, wrapper);
        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(10));

        return new PageResult(result.getTotal(), result.getRecords());
    }

    // ============ HR：编辑职位 ============
    public void updateMyJob(Long enterpriseId, JobPosition jobPosition) {
        JobPosition existing = this.getById(jobPosition.getId());
        if (existing == null || !existing.getEnterpriseId().equals(enterpriseId)) {
            throw new RuntimeException("职位不存在或无权限修改");
        }
        this.updateById(jobPosition);

        // 失效相关缓存
        redisTemplate.delete("hr:job:list:enterprise:" + enterpriseId + ":*");
        redisTemplate.delete("seeker:job:detail:" + jobPosition.getId());
    }

    // ============ HR：下架职位 ============
    public void deleteMyJob(Long enterpriseId, Long jobId) {
        JobPosition job = this.getById(jobId);
        if (job == null || !job.getEnterpriseId().equals(enterpriseId)) {
            throw new RuntimeException("职位不存在或无权限操作");
        }
        job.setStatus((byte) 2);  // 2=已结束/下架
        this.updateById(job);

        // 失效缓存
        redisTemplate.delete("hr:job:list:enterprise:" + enterpriseId + ":*");
        redisTemplate.delete("seeker:job:detail:" + jobId);
    }

    // ============ HR：我的职位详情 ============
    public JobPosition getMyJobDetail(Long enterpriseId, Long jobId) {
        JobPosition job = this.getById(jobId);
        if (job == null || !job.getEnterpriseId().equals(enterpriseId)) {
            throw new RuntimeException("职位不存在或无权限查看");
        }
        return job;
    }
}