package com.school_enterprise_platform.controller.counselor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.Resume;
import com.school_enterprise_platform.entity.StudentAnalysis;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.CounselorService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 辅导员控制器
 * 功能：管理所带学生信息、简历、学习进度、投递记录、统计等
 */
@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    /**
     * 获取所带学生列表（分页）
     */
    @GetMapping("/student/list")
    public Result<PageResult> getMyStudents(Page page) {
        Long counselorUserId = BaseContext.getCurrentId();
        PageResult result = counselorService.getMyStudents(counselorUserId, page);
        return Result.success(result);
    }

    /**
     * 查看指定学生简历列表（分页）
     */
    @GetMapping("/student/resume/{studentUserId}")
    public Result<PageResult> getStudentResumes(
            @PathVariable Long studentUserId,
            Page<Resume> page) {
        Long counselorUserId = BaseContext.getCurrentId();
        PageResult result = counselorService.getStudentResumes(counselorUserId, studentUserId, page);
        return Result.success(result);
    }

    /**
     * 查看指定学生学习进度（报名数 + 完成数）
     */
    @GetMapping("/student/progress/{studentUserId}")
    public Result<Object> getStudentProgress(@PathVariable Long studentUserId) {
        Long counselorUserId = BaseContext.getCurrentId();
        Object progress = counselorService.getStudentLearningProgress(counselorUserId, studentUserId);
        return Result.success(progress);
    }

    /**
     * 查看指定学生投递记录（分页）
     */
    @GetMapping("/student/job/{studentUserId}")
    public Result<PageResult> getStudentJobApplications(
            @PathVariable Long studentUserId,
            Page page) {
        Long counselorUserId = BaseContext.getCurrentId();
        PageResult result = counselorService.getStudentJobApplications(counselorUserId, studentUserId, page);
        return Result.success(result);
    }

    /**
     * 查看指定学生统计汇总（student_analysis 表）
     */
    @GetMapping("/student/analysis/{studentId}")
    public Result<StudentAnalysis> getStudentAnalysis(@PathVariable Long studentId) {
        Long counselorUserId = BaseContext.getCurrentId();
        StudentAnalysis analysis = counselorService.getStudentAnalysis(counselorUserId, studentId);
        return Result.success(analysis);
    }

    /**
     * 获取班级整体统计（学生数、就业率等）
     */
    @GetMapping("/class/stats")
    public Result<Object> getClassStats() {
        Long counselorUserId = BaseContext.getCurrentId();
        Object stats = counselorService.getClassStatistics(counselorUserId);
        return Result.success(stats);
    }
}