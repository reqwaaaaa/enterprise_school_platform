package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.entity.*;
import com.school_enterprise_platform.mapper.*;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.CounselorService;
import com.school_enterprise_platform.service.ResumeService;
import com.school_enterprise_platform.service.JobApplicationService;
import com.school_enterprise_platform.service.CourseEnrollmentService;
import com.school_enterprise_platform.service.LearningRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;  // 新增：Redis Duration
import java.util.HashMap;
import java.util.Map;

@Service
public class CounselorServiceImpl implements CounselorService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private CourseEnrollmentService courseEnrollmentService;

    @Autowired
    private LearningRecordService learningRecordService;

    @Autowired
    private StudentAnalysisMapper studentAnalysisMapper;  // 新增注入

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private void validateCounselorPermission(Long counselorUserId, Long studentId) {
        Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>()
                .eq(Student::getId, studentId)
                .eq(Student::getCounselorId, counselorUserId));
        if (student == null) {
            throw new RuntimeException("无权限查看该学生信息");
        }
    }

    @Override
    public PageResult getMyStudents(Long counselorUserId, Page page) {
        String key = "counselor:student:list:" + counselorUserId + ":page:" + page.getCurrent();

        @SuppressWarnings("unchecked")
        Page<User> cached = (Page<User>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        Page<User> result = studentMapper.selectStudentPageByCounselor(page, counselorUserId);

        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(20));

        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Override
    public PageResult getStudentResumes(Long counselorUserId, Long studentUserId, Page<Resume> page) {
        validateCounselorPermission(counselorUserId, studentUserId);
        return resumeService.getMyResumes(studentUserId, page);
    }

    @Override
    public Object getStudentLearningProgress(Long counselorUserId, Long studentUserId) {
        validateCounselorPermission(counselorUserId, studentUserId);

        Map<String, Object> progress = new HashMap<>();
        progress.put("enrolledCourses", courseEnrollmentService.lambdaQuery()
                .eq(CourseEnrollment::getUserId, studentUserId)
                .count());
        progress.put("completedCourses", learningRecordService.lambdaQuery()
                .eq(LearningRecord::getUserId, studentUserId)
                .eq(LearningRecord::getStatus, (byte) 2)
                .count());

        return progress;
    }

    @Override
    public PageResult getStudentJobApplications(Long counselorUserId, Long studentUserId, Page page) {
        validateCounselorPermission(counselorUserId, studentUserId);

        LambdaQueryWrapper<JobApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JobApplication::getUserId, studentUserId)
                .orderByDesc(JobApplication::getApplyTime);

        Page<JobApplication> result = jobApplicationService.page(page, wrapper);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Override
    public StudentAnalysis getStudentAnalysis(Long counselorUserId, Long studentId) {
        validateCounselorPermission(counselorUserId, studentId);
        return studentAnalysisMapper.selectById(studentId);  // 现在可识别
    }

    @Override
    public Object getClassStatistics(Long counselorUserId) {
        Map<String, Object> stats = new HashMap<>();

        Long studentCount = studentMapper.selectCount(new LambdaQueryWrapper<Student>()
                .eq(Student::getCounselorId, counselorUserId));

        // 可后续扩展更多统计
        stats.put("studentCount", studentCount == null ? 0 : studentCount);
        stats.put("employmentRate", 0.0);  // 占位，实际可计算

        return stats;
    }
}