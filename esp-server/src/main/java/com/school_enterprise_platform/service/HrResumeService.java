package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.JobApplication;
import com.school_enterprise_platform.result.PageResult;

public interface HrResumeService {
    PageResult getReceivedResumes(Long enterpriseId, Page<JobApplication> page);

    JobApplication getResumeDetail(Long enterpriseId, Long applyId);
}