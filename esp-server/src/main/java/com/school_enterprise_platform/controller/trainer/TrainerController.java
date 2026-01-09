package com.school_enterprise_platform.controller.trainer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.dto.TrainerCourseStatsDTO;
import com.school_enterprise_platform.entity.Course;
import com.school_enterprise_platform.entity.CourseChapter;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.CourseChapterService;
import com.school_enterprise_platform.service.CourseService;
import com.school_enterprise_platform.service.TrainerCourseService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private TrainerCourseService trainerCourseService;  // 专用服务（我的课程管理）

    @Autowired
    private CourseChapterService courseChapterService;

    // ============ 我的课程管理 ============

    @GetMapping("/course/list")
    public Result<PageResult> getMyCourses(Page<Course> page) {
        Long trainerId = BaseContext.getCurrentId();
        PageResult result = trainerCourseService.getMyCourses(trainerId, page);
        return Result.success(result);
    }

    @PostMapping("/course/add")
    public Result<String> addCourse(@RequestBody Course course) {
        Long trainerId = BaseContext.getCurrentId();
        trainerCourseService.addCourse(trainerId, course);
        return Result.success("创建成功");
    }

    @PutMapping("/course/update")
    public Result<String> updateCourse(@RequestBody Course course) {
        Long trainerId = BaseContext.getCurrentId();
        trainerCourseService.updateCourse(trainerId, course);
        return Result.success("修改成功");
    }

    @DeleteMapping("/course/delete/{id}")
    public Result<String> deleteCourse(@PathVariable Long id) {
        Long trainerId = BaseContext.getCurrentId();
        trainerCourseService.deleteCourse(trainerId, id);
        return Result.success("删除成功");
    }

    @GetMapping("/course/detail/{id}")
    public Result<Course> getCourseDetail(@PathVariable Long id) {
        Long trainerId = BaseContext.getCurrentId();
        Course course = trainerCourseService.getMyCourseDetail(trainerId, id);
        return Result.success(course);
    }

    // ============ 章节管理 ============

    @GetMapping("/course/chapter/list/{courseId}")
    public Result<PageResult> getCourseChapters(@PathVariable Long courseId, Page<CourseChapter> page) {
        Long trainerId = BaseContext.getCurrentId();
        trainerCourseService.validateCourseOwnership(trainerId, courseId);  // 权限校验
        PageResult result = courseChapterService.getCourseChapters(courseId, page);
        return Result.success(result);
    }

    @PostMapping("/course/chapter/add")
    public Result<String> addChapter(@RequestBody CourseChapter chapter) {
        Long trainerId = BaseContext.getCurrentId();
        trainerCourseService.addChapter(trainerId, chapter);
        return Result.success("添加成功");
    }

    @PutMapping("/course/chapter/update")
    public Result<String> updateChapter(@RequestBody CourseChapter chapter) {
        Long trainerId = BaseContext.getCurrentId();
        trainerCourseService.updateChapter(trainerId, chapter);
        return Result.success("修改成功");
    }

    @DeleteMapping("/course/chapter/delete/{id}")
    public Result<String> deleteChapter(@PathVariable Long id) {
        Long trainerId = BaseContext.getCurrentId();
        trainerCourseService.deleteChapter(trainerId, id);
        return Result.success("删除成功");
    }

    // ============ 学员统计 ============

    @GetMapping("/course/stats/{courseId}")
    public Result<TrainerCourseStatsDTO> getCourseStats(@PathVariable Long courseId) {
        Long trainerId = BaseContext.getCurrentId();
        TrainerCourseStatsDTO stats = trainerCourseService.getCourseStats(trainerId, courseId);
        return Result.success(stats);
    }
}