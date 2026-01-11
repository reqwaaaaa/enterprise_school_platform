package com.school_enterprise_platform.controller.student;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.dto.SeekerStatisticsDTO;
import com.school_enterprise_platform.entity.Course;
import com.school_enterprise_platform.entity.CourseChapter;
import com.school_enterprise_platform.entity.JobPosition;
import com.school_enterprise_platform.entity.Resume;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.*;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 在校学生控制器
 * 功能：简历管理（复用求职者）、职位搜索/投递（复用求职者）、本校+公共课程搜索/报名/学习章节、个人统计（复用求职者）
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private StudentCourseService studentCourseService; // 专用：本校 + 公共课程

    @Autowired
    private CourseChapterService courseChapterService;

    @Autowired
    private CourseEnrollmentService courseEnrollmentService;

    @Autowired
    private SeekerStatisticsService statisticsService; // 复用求职者统计

    // ============ 简历相关（完全复用 seeker） ============

    /**
     * 获取我的简历列表（分页）
     */
    @GetMapping("/resume/list")
    public Result<PageResult> getMyResumes(Page<Resume> page) {
        Long userId = BaseContext.getCurrentId();
        PageResult result = resumeService.getMyResumes(userId, page);
        return Result.success(result);
    }

    /**
     * 获取简历详情
     */
    @GetMapping("/resume/detail/{id}")
    public Result<Resume> getResumeDetail(@PathVariable Long id) {
        Long userId = BaseContext.getCurrentId();
        Resume resume = resumeService.getResumeDetail(userId, id);
        return Result.success(resume);
    }

    /**
     * 新增简历
     */
    @PostMapping("/resume/add")
    public Result<String> addResume(@RequestBody Resume resume) {
        Long userId = BaseContext.getCurrentId();
        resumeService.addResume(userId, resume);
        return Result.success("新增成功");
    }

    /**
     * 编辑简历
     */
    @PutMapping("/resume/update")
    public Result<String> updateResume(@RequestBody Resume resume) {
        Long userId = BaseContext.getCurrentId();
        resumeService.updateResume(userId, resume);
        return Result.success("修改成功");
    }

    /**
     * 删除简历
     */
    @DeleteMapping("/resume/delete/{id}")
    public Result<String> deleteResume(@PathVariable Long id) {
        Long userId = BaseContext.getCurrentId();
        resumeService.deleteResume(userId, id);
        return Result.success("删除成功");
    }

    // ============ 职位相关（完全复用 seeker） ============

    /**
     * 职位搜索（支持关键词 + 标签）
     */
    @GetMapping("/job/search")
    public Result<PageResult> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tags,
            Page<JobPosition> page) {
        PageResult result = jobPositionService.searchJobs(keyword, tags, page);
        return Result.success(result);
    }

    /**
     * 职位详情
     */
    @GetMapping("/job/detail/{id}")
    public Result<JobPosition> getJobDetail(@PathVariable Long id) {
        JobPosition job = jobPositionService.getJobDetail(id);
        return Result.success(job);
    }

    /**
     * 投递职位（选择简历）
     */
    @PostMapping("/job/apply")
    public Result<String> applyJob(
            @RequestParam Long jobId,
            @RequestParam Long resumeId) {
        Long userId = BaseContext.getCurrentId();
        jobApplicationService.applyJob(userId, jobId, resumeId);
        return Result.success("投递成功");
    }

    // ============ 课程相关（专用：本校 + 公共） ============

    /**
     * 本校 + 公共课程搜索
     */
    @GetMapping("/course/search")
    public Result<PageResult> searchCourses(
            @RequestParam(required = false) String keyword,
            Page<Course> page) {
        Long userId = BaseContext.getCurrentId();
        PageResult result = studentCourseService.searchMySchoolAndPublicCourses(userId, keyword, page);
        return Result.success(result);
    }

    /**
     * 课程详情（本校/公共均可）
     */
    @GetMapping("/course/detail/{id}")
    public Result<Course> getCourseDetail(@PathVariable Long id) {
        Course course = studentCourseService.getCourseDetail(id);
        return Result.success(course);
    }

    /**
     * 课程报名（本校/公共均可）
     */
    @PostMapping("/course/enroll")
    public Result<String> enrollCourse(@RequestParam Long courseId) {
        Long userId = BaseContext.getCurrentId();
        courseEnrollmentService.enrollCourse(userId, courseId);
        return Result.success("报名成功");
    }

    /**
     * 在线学习章节列表
     */
    @GetMapping("/course/learn/{courseId}")
    public Result<PageResult> getCourseChapters(
            @PathVariable Long courseId,
            Page<CourseChapter> page) {
        PageResult result = courseChapterService.getCourseChapters(courseId, page);
        return Result.success(result);
    }

    // ============ 个人统计（复用求职者统计） ============

    /**
     * 获取学生个人统计（简历数、投递数、课程完成等）
     */
    @GetMapping("/statistics")
    public Result<SeekerStatisticsDTO> getStatistics() {
        Long userId = BaseContext.getCurrentId();
        SeekerStatisticsDTO dto = statisticsService.getStatistics(userId);
        return Result.success(dto);
    }
}