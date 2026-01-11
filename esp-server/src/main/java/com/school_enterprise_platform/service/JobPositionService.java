package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school_enterprise_platform.entity.JobPosition;
import com.school_enterprise_platform.result.PageResult;

import java.util.Map;

public interface JobPositionService extends IService<JobPosition> {

    // 求职者接口（已存在）
    PageResult searchJobs(String keyword, String tags, Page<JobPosition> page);

    JobPosition getJobDetail(Long jobId);

    // 新增：HR 专用接口
    PageResult getMyJobs(Long enterpriseId, Page<JobPosition> page);

    void updateMyJob(Long enterpriseId, JobPosition jobPosition);

    void deleteMyJob(Long enterpriseId, Long jobId);

    JobPosition getMyJobDetail(Long enterpriseId, Long jobId);

    Map<String, Object> getPlatformEmploymentStats();
}