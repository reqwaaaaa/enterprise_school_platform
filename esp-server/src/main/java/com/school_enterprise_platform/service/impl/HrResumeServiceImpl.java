package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;  // 正确导入
import com.school_enterprise_platform.entity.JobApplication;
import com.school_enterprise_platform.entity.JobPosition;  // 新增：实体类
import com.school_enterprise_platform.mapper.JobApplicationMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.HrResumeService;
import com.school_enterprise_platform.service.JobPositionService;  // 新增：服务接口
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class HrResumeServiceImpl extends ServiceImpl<JobApplicationMapper, JobApplication> implements HrResumeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JobPositionService jobPositionService;  // 关键注入

    @Override
    public PageResult getReceivedResumes(Long enterpriseId, Page<JobApplication> page) {
        String key = "hr:resume:received:enterprise:" + enterpriseId + ":page:" + page.getCurrent();

        @SuppressWarnings("unchecked")
        Page<JobApplication> cached = (Page<JobApplication>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        LambdaQueryWrapper<JobApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.inSql(JobApplication::getJobId, "SELECT id FROM job_position WHERE enterprise_id = " + enterpriseId)
                .orderByDesc(JobApplication::getApplyTime);

        Page<JobApplication> result = this.page(page, wrapper);

        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(15));

        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Override
    public JobApplication getResumeDetail(Long enterpriseId, Long applyId) {
        String key = "hr:resume:detail:" + applyId;

        @SuppressWarnings("unchecked")
        JobApplication application = (JobApplication) redisTemplate.opsForValue().get(key);
        if (application == null) {
            application = this.getById(applyId);
            if (application == null) {
                throw new RuntimeException("投递记录不存在");
            }

            // 校验企业权限：投递的职位必须属于该企业
            JobPosition job = jobPositionService.getById(application.getJobId());
            if (job == null || !job.getEnterpriseId().equals(enterpriseId)) {
                throw new RuntimeException("无权限查看该简历");
            }

            redisTemplate.opsForValue().set(key, application, Duration.ofHours(1));
        }
        return application;
    }
}