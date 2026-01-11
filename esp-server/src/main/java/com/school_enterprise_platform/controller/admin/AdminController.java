package com.school_enterprise_platform.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.dto.DeclarationAuditDTO;
import com.school_enterprise_platform.entity.*;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.*;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 政府管理员控制器
 * 补贴审核、全平台统计、用户管理、日志审计、内容删除、公告发布
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DeclarationService declarationService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SystemLogService systemLogService;

    @Autowired
    private AdminSearchService adminSearchService;

    // ============ 补贴申报管理 ============

    /**
     * 获取所有补贴申报列表（支持按状态过滤）
     */
    @GetMapping("/declare/list")
    public Result<PageResult> getDeclarationList(
            @RequestParam(required = false) Byte status,
            Page<Declaration> page) {
        checkAdminRole();
        PageResult result = declarationService.getDeclarationList(status, page);
        return Result.success(result);
    }

    /**
     * 审核补贴申报
     */
    @PostMapping("/declare/{id}/audit")
    public Result<String> auditDeclaration(
            @PathVariable Long id,
            @RequestBody DeclarationAuditDTO auditDTO) {
        checkAdminRole();
        // 自动填充审核人ID
        auditDTO.setReviewerId(BaseContext.getCurrentId());
        declarationService.auditDeclaration(id, auditDTO);
        return Result.success("审核完成");
    }

    // ============ 全平台统计报表 ============

    /**
     * 全平台申报统计
     */
    @GetMapping("/statistics/declare")
    public Result<Map<String, Object>> getDeclareStatistics() {
        checkAdminRole();
        Map<String, Object> stats = declarationService.getPlatformDeclareStats();
        return Result.success(stats);
    }

    /**
     * 全平台培训统计
     */
    @GetMapping("/statistics/training")
    public Result<Map<String, Object>> getTrainingStatistics() {
        checkAdminRole();
        Map<String, Object> stats = courseService.getPlatformTrainingStats();
        return Result.success(stats);
    }

    /**
     * 全平台就业统计
     */
    @GetMapping("/statistics/employment")
    public Result<Map<String, Object>> getEmploymentStatistics() {
        checkAdminRole();
        Map<String, Object> stats = jobPositionService.getPlatformEmploymentStats();
        return Result.success(stats);
    }

    // ============ 平台公告发布 ============

    /**
     * 发布平台公告（广播系统通知）
     */
    @PostMapping("/announce")
    public Result<String> publishAnnouncement(@RequestBody Map<String, String> request) {
        checkAdminRole();
        String content = request.get("content");
        if (content == null || content.trim().isEmpty()) {
            return Result.error("公告内容不能为空");
        }
        userService.publishBroadcastMessage(content);
        return Result.success("公告已发布");
    }

    // ============ 全站搜索 ============

    /**
     * 全站信息搜索（支持多种类型）
     */
    @GetMapping("/search/all")
    public Result<PageResult> globalSearch(
            @RequestParam String keyword,
            @RequestParam String type,
            Page page) {
        checkAdminRole();
        PageResult result = adminSearchService.globalSearch(keyword, type, page);
        return Result.success(result);
    }

    // ============ 强制删除不合规内容 ============

    /**
     * 强制删除不合规内容（支持 course / user_message 等）
     */
    @DeleteMapping("/content/delete/{type}/{id}")
    public Result<String> deleteContent(
            @PathVariable String type,
            @PathVariable Long id) {
        checkAdminRole();

        switch (type.toLowerCase()) {
            case "course":
                Course course = courseService.getById(id);
                if (course != null) {
                    course.setStatus((byte) 2); // 软删除
                    courseService.updateById(course);
                }
                break;
            case "user_message":
            case "message":
                messageService.removeById(id);
                break;
            default:
                return Result.error("不支持的删除类型");
        }
        return Result.success("删除成功");
    }

    // ============ 系统用户管理 ============

    /**
     * 系统用户列表（分页）
     */
    @GetMapping("/users")
    public Result<PageResult<User>> getUsers(Page<User> page) {
        checkAdminRole();
        Page<User> result = userService.page(page);
        return Result.success(new PageResult(result.getTotal(), result.getRecords()));
    }

    /**
     * 禁用用户
     */
    @PutMapping("/users/disable/{id}")
    public Result<String> disableUser(@PathVariable Long id) {
        checkAdminRole();
        userService.disableUser(id);
        return Result.success("用户已禁用");
    }

    // ============ 日志审计查看 ============

    /**
     * 系统日志查询（支持按用户ID过滤）
     */
    @GetMapping("/logs")
    public Result<PageResult<SystemLog>> getLogs(
            Page<SystemLog> page,
            @RequestParam(required = false) Long userId) {
        checkAdminRole();
        PageResult result = systemLogService.getLogs(page, userId);
        return Result.success(result);
    }

    // ============ 权限校验工具方法 ============

    private void checkAdminRole() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new RuntimeException("未登录");
        }
        User user = userService.getById(userId);
        if (user == null || user.getUserRole() != 6) {
            throw new RuntimeException("无管理员权限");
        }
    }
}