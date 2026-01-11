package com.school_enterprise_platform.controller.hr;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.JobApplication;
import com.school_enterprise_platform.entity.JobPosition;
import com.school_enterprise_platform.entity.User;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.HrResumeService;
import com.school_enterprise_platform.service.JobPositionService;
import com.school_enterprise_platform.service.UserService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 企业HR控制器
 * 功能：职位管理（发布/编辑/下架/详情/列表）、收到简历查看
 */
@RestController
@RequestMapping("/hr")
public class HrController {

    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private HrResumeService hrResumeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;  // 用于缓存失效

    // ============ 职位管理 ============

    /**
     * 获取我的职位列表（分页）
     */
    @GetMapping("/job/list")
    public Result<PageResult> getMyJobs(Page<JobPosition> page) {
        Long enterpriseId = getCurrentEnterpriseId();
        PageResult result = jobPositionService.getMyJobs(enterpriseId, page);
        return Result.success(result);
    }

    /**
     * 发布新职位
     */
    @PostMapping("/job/add")
    public Result<String> addJob(@RequestBody JobPosition jobPosition) {
        Long enterpriseId = getCurrentEnterpriseId();
        jobPosition.setEnterpriseId(enterpriseId);
        jobPosition.setStatus((byte) 1);  // 已发布
        jobPosition.setPublishTime(LocalDateTime.now());
        jobPositionService.save(jobPosition);

        // 发布后立即失效 HR 职位列表缓存
        redisTemplate.delete("hr:job:list:enterprise:" + enterpriseId + ":*");

        return Result.success("发布成功");
    }

    /**
     * 编辑职位
     */
    @PutMapping("/job/update")
    public Result<String> updateJob(@RequestBody JobPosition jobPosition) {
        Long enterpriseId = getCurrentEnterpriseId();
        jobPositionService.updateMyJob(enterpriseId, jobPosition);
        return Result.success("修改成功");
    }

    /**
     * 下架职位（改为 status=2）
     */
    @DeleteMapping("/job/delete/{id}")
    public Result<String> deleteJob(@PathVariable Long id) {
        Long enterpriseId = getCurrentEnterpriseId();
        jobPositionService.deleteMyJob(enterpriseId, id);
        return Result.success("下架成功");
    }

    /**
     * 查看职位详情（HR视角）
     */
    @GetMapping("/job/detail/{id}")
    public Result<JobPosition> getJobDetail(@PathVariable Long id) {
        Long enterpriseId = getCurrentEnterpriseId();
        JobPosition job = jobPositionService.getMyJobDetail(enterpriseId, id);
        return Result.success(job);
    }

    // ============ 收到简历 ============

    /**
     * 获取收到简历列表（分页）
     */
    @GetMapping("/resume/received")
    public Result<PageResult> getReceivedResumes(Page<JobApplication> page) {
        Long enterpriseId = getCurrentEnterpriseId();
        PageResult result = hrResumeService.getReceivedResumes(enterpriseId, page);
        return Result.success(result);
    }

    /**
     * 查看简历详情（投递记录）
     */
    @GetMapping("/resume/detail/{applyId}")
    public Result<JobApplication> getResumeDetail(@PathVariable Long applyId) {
        Long enterpriseId = getCurrentEnterpriseId();
        JobApplication application = hrResumeService.getResumeDetail(enterpriseId, applyId);
        return Result.success(application);
    }

    // ============ 工具方法：获取当前HR所属企业ID ============

    private Long getCurrentEnterpriseId() {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);
        if (user == null || user.getEnterpriseId() == null) {
            throw new RuntimeException("未绑定企业，无法操作");
        }
        return user.getEnterpriseId();
    }
}