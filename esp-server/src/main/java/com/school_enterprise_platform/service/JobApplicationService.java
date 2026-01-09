package com.school_enterprise_platform.service;

import com.school_enterprise_platform.entity.JobApplication;
import com.baomidou.mybatisplus.extension.service.IService;

public interface JobApplicationService extends IService<JobApplication> {
    void applyJob(Long userId, Long jobId, Long resumeId);
}