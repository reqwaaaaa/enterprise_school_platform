package com.school_enterprise_platform.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.dto.DeclarationAuditDTO;
import com.school_enterprise_platform.entity.Declaration;
import com.school_enterprise_platform.entity.User;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.DeclarationService;
import com.school_enterprise_platform.service.UserService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DeclarationService declarationService;

    @Autowired
    private UserService userService;

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
     * 全平台申报统计（已实现）
     */
    @GetMapping("/statistics/declare")
    public Result<Map<String, Object>> getDeclareStatistics() {
        checkAdminRole();
        Map<String, Object> stats = declarationService.getPlatformDeclareStats();
        return Result.success(stats);
    }

    /**
     * 全平台培训统计（暂未实现，占位）
     */
    @GetMapping("/statistics/training")
    public Result<Map<String, Object>> getTrainingStatistics() {
        checkAdminRole();
        // TODO: 后续在 CourseService 中实现 getPlatformTrainingStats()
        Map<String, Object> placeholder = Map.of("message", "培训统计功能开发中");
        return Result.success(placeholder);
    }

    /**
     * 全平台就业统计（暂未实现，占位）
     */
    @GetMapping("/statistics/employment")
    public Result<Map<String, Object>> getEmploymentStatistics() {
        checkAdminRole();
        // TODO: 后续在 JobPositionService 中实现 getPlatformEmploymentStats()
        Map<String, Object> placeholder = Map.of("message", "就业统计功能开发中");
        return Result.success(placeholder);
    }

    // ============ 平台公告发布 ============

    /**
     * 发布平台公告（广播系统通知）
     * 暂未实现 publishBroadcastMessage 方法，占位返回
     */
    @PostMapping("/announce")
    public Result<String> publishAnnouncement(@RequestBody Map<String, String> request) {
        checkAdminRole();
        String content = request.get("content");
        if (content == null || content.isBlank()) {
            return Result.error("公告内容不能为空");
        }
        // TODO: 后续在 UserService 中实现 publishBroadcastMessage(content)
        return Result.success("公告发布功能开发中（内容已接收）");
    }

    // ============ 全站搜索（简化版，暂未实现） ============

    @GetMapping("/search/all")
    public Result<PageResult> globalSearch(
            @RequestParam String keyword,
            @RequestParam String type,
            Page page) {
        checkAdminRole();
        // TODO: 后续实现全站搜索（可结合 MyBatis-Plus 多表查询或 ES）
        return Result.success(new PageResult(0L, java.util.Collections.emptyList()));
    }

    // ============ 强制删除不合规内容 ============

    @DeleteMapping("/content/delete/{type}/{id}")
    public Result<String> deleteContent(@PathVariable String type, @PathVariable Long id) {
        checkAdminRole();
        // TODO: 后续实现具体删除逻辑
        return Result.success("删除功能开发中（type: " + type + ", id: " + id + ")");
    }

    // ============ 系统用户管理 ============

    @GetMapping("/users")
    public Result<PageResult> getUsers(Page<User> page) {
        checkAdminRole();
        // TODO: 后续实现用户分页查询
        return Result.success(new PageResult(0L, java.util.Collections.emptyList()));
    }

    @PutMapping("/users/disable/{id}")
    public Result<String> disableUser(@PathVariable Long id) {
        checkAdminRole();
        // TODO: 后续在 UserService 中实现 disableUser(id)
        return Result.success("禁用功能开发中（userId: " + id + ")");
    }

    // ============ 日志审计查看（简化） ============

    @GetMapping("/logs")
    public Result<PageResult> getLogs(Page page,
                                      @RequestParam(required = false) Long userId) {
        checkAdminRole();
        // TODO: 后续实现 system_log 查询
        return Result.success(new PageResult(0L, java.util.Collections.emptyList()));
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