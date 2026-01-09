package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.Resume;
import com.school_enterprise_platform.entity.StudentAnalysis;
import com.school_enterprise_platform.result.PageResult;

public interface CounselorService {

    PageResult getMyStudents(Long counselorUserId, Page page);

    PageResult getStudentResumes(Long counselorUserId, Long studentUserId, Page<Resume> page);

    Object getStudentLearningProgress(Long counselorUserId, Long studentUserId);

    PageResult getStudentJobApplications(Long counselorUserId, Long studentUserId, Page page);

    StudentAnalysis getStudentAnalysis(Long counselorUserId, Long studentId);

    Object getClassStatistics(Long counselorUserId);
}