package com.school_enterprise_platform.controller.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.User;
import com.school_enterprise_platform.entity.UserMessage;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.MessageService;
import com.school_enterprise_platform.service.UploadService;
import com.school_enterprise_platform.service.UserService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 公共模块控制器
 * 处理个人信息、消息、文件上传等公共接口（所有角色通用）
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UploadService uploadService;

    /**
     * 获取当前用户个人信息
     */
    @GetMapping("/profile")
    public Result<User> getProfile() {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getProfile(userId);
        return Result.success(user);
    }

    /**
     * 修改当前用户个人信息
     */
    @PutMapping("/profile")
    public Result<String> updateProfile(@RequestBody User user) {
        user.setId(BaseContext.getCurrentId());
        userService.updateProfile(user);
        return Result.success("修改成功");
    }

    /**
     * 获取当前用户消息列表（私信 + 课程通知）
     */
    @GetMapping("/messages")
    public Result<PageResult> getMessages(Page<UserMessage> page) {
        Long userId = BaseContext.getCurrentId();
        Page<UserMessage> pageResult = messageService.getMessages(userId, page);
        PageResult result = new PageResult(pageResult.getTotal(), pageResult.getRecords());
        return Result.success(result);
    }

    /**
     * 获取消息详情并标记已读
     */
    @GetMapping("/messages/{id}")
    public Result<UserMessage> getMessageDetail(@PathVariable Long id) {
        Long userId = BaseContext.getCurrentId();
        UserMessage message = messageService.getMessageDetail(userId, id);
        return Result.success(message);
    }

    /**
     * 获取系统通知列表（全局广播）
     */
    @GetMapping("/notifications")
    public Result<PageResult> getNotifications(Page<UserMessage> page) {
        Page<UserMessage> pageResult = messageService.getNotifications(page);
        PageResult result = new PageResult(pageResult.getTotal(), pageResult.getRecords());
        return Result.success(result);
    }

    /**
     * 文件上传接口（支持头像、课件、简历附件等）
     * @param file 上传的文件
     * @return COS 访问 URL
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        try {
            String url = uploadService.upload(file);
            return Result.success(url);
        } catch (Exception e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}