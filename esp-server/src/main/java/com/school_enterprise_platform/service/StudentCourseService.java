package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.Course;
import com.school_enterprise_platform.result.PageResult;

public interface StudentCourseService {
    /**
     * 在校学生课程搜索：本校课程 + 公共课程
     */
    PageResult searchMySchoolAndPublicCourses(Long userId, String keyword, Page<Course> page);

    /**
     * 课程详情（复用逻辑，但保持接口一致）
     */
    Course getCourseDetail(Long courseId);
}