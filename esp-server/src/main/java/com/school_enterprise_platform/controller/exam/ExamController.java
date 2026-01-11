package com.school_enterprise_platform.controller.exam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.Exam;
import com.school_enterprise_platform.entity.ExamRecord;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.ExamService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 考试控制器
 */
@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    /**
     * 提交考试问卷
     */
    @PostMapping("/submit")
    public Result<String> submitExam(
            @RequestParam Long examId,
            @RequestParam String answers) {
        Long userId = BaseContext.getCurrentId();
        examService.submitExam(userId, examId, answers);
        return Result.success("提交成功");
    }

    /**
     * 获取考试列表（讲师/管理员）
     */
    @GetMapping("/list")
    public Result<PageResult<Exam>> getExamList(
            @RequestParam(required = false) Byte status,
            Page<Exam> page) {
        // 权限校验：仅讲师/管理员（可扩展）
        PageResult<Exam> result = examService.getExamList(page, status);
        return Result.success(result);
    }

    /**
     * 获取我的考试记录（用户/辅导员）
     */
    @GetMapping("/my-records")
    public Result<PageResult<ExamRecord>> getMyExamRecords(Page<ExamRecord> page) {
        Long userId = BaseContext.getCurrentId();
        PageResult<ExamRecord> result = examService.getUserExamRecords(userId, page);
        return Result.success(result);
    }
}