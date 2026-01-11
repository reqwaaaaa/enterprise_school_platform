package com.school_enterprise_platform.service;

import com.school_enterprise_platform.entity.Exam;
import com.school_enterprise_platform.entity.ExamRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school_enterprise_platform.result.PageResult;

/**
 * 考试服务接口（扩展原有 submitExam）
 */
public interface ExamService extends IService<ExamRecord> {

    /**
     * 提交考试问卷（原有）
     */
    void submitExam(Long userId, Long examId, String answers);

    /**
     * 获取考试列表（讲师/管理员查看）
     */
    PageResult<Exam> getExamList(Page<Exam> page, Byte status);

    /**
     * 获取用户考试记录（用户/辅导员查看）
     */
    PageResult<ExamRecord> getUserExamRecords(Long userId, Page<ExamRecord> page);
}