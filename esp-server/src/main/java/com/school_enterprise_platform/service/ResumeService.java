package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.Resume;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school_enterprise_platform.result.PageResult;

public interface ResumeService extends IService<Resume> {
    PageResult getMyResumes(Long userId, Page<Resume> page);

    Resume getResumeDetail(Long userId, Long resumeId);

    void addResume(Long userId, Resume resume);

    void updateResume(Long userId, Resume resume);

    void deleteResume(Long userId, Long resumeId);
}