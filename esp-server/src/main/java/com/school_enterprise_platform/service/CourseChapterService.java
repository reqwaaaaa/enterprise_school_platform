package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.CourseChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school_enterprise_platform.result.PageResult;

public interface CourseChapterService extends IService<CourseChapter> {
    PageResult getCourseChapters(Long courseId, Page<CourseChapter> page);
}