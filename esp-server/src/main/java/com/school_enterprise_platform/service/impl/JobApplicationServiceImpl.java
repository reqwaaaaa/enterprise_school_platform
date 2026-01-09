package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.entity.JobApplication;
import com.school_enterprise_platform.mapper.JobApplicationMapper;
import com.school_enterprise_platform.service.JobApplicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JobApplicationServiceImpl extends ServiceImpl<JobApplicationMapper, JobApplication> implements JobApplicationService {

    @Override
    public void applyJob(Long userId, Long jobId, Long resumeId) {
        JobApplication application = new JobApplication();
        application.setUserId(userId);
        application.setJobId(jobId);
        application.setResumeId(resumeId);
        application.setApplyTime(LocalDateTime.now());
        application.setStatus((byte) 0);  // 0待处理

        this.save(application);
    }
}