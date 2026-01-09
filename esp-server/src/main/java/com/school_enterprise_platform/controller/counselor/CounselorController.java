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

@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    // 所带学生列表
    @GetMapping("/student/list")
    public Result<PageResult> getMyStudents(Page page) {
        Long counselorUserId = BaseContext.getCurrentId();
        PageResult result = counselorService.getMyStudents(counselorUserId, page);
        return Result.success(result);
    }

    // 查看学生简历列表
    @GetMapping("/student/resume/{studentUserId}")
    public Result<PageResult> getStudentResumes(@PathVariable Long studentUserId, Page<Resume> page) {
        Long counselorUserId = BaseContext.getCurrentId();
        PageResult result = counselorService.getStudentResumes(counselorUserId, studentUserId, page);
        return Result.success(result);
    }

    // 查看学生学习进度
    @GetMapping("/student/progress/{studentUserId}")
    public Result<Object> getStudentProgress(@PathVariable Long studentUserId) {
        Long counselorUserId = BaseContext.getCurrentId();
        Object progress = counselorService.getStudentLearningProgress(counselorUserId, studentUserId);
        return Result.success(progress);
    }

    // 查看学生投递记录
    @GetMapping("/student/job/{studentUserId}")
    public Result<PageResult> getStudentJobApplications(@PathVariable Long studentUserId, Page page) {
        Long counselorUserId = BaseContext.getCurrentId();
        PageResult result = counselorService.getStudentJobApplications(counselorUserId, studentUserId, page);
        return Result.success(result);
    }

    // 查看学生统计汇总
    @GetMapping("/student/analysis/{studentId}")
    public Result<StudentAnalysis> getStudentAnalysis(@PathVariable Long studentId) {
        Long counselorUserId = BaseContext.getCurrentId();
        StudentAnalysis analysis = counselorService.getStudentAnalysis(counselorUserId, studentId);
        return Result.success(analysis);
    }

    // 班级整体统计
    @GetMapping("/class/stats")
    public Result<Object> getClassStats() {
        Long counselorUserId = BaseContext.getCurrentId();
        Object stats = counselorService.getClassStatistics(counselorUserId);
        return Result.success(stats);
    }
}