package com.school_enterprise_platform.controller.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.User;
import com.school_enterprise_platform.entity.UserMessage;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.MessageService;
import com.school_enterprise_platform.service.UserService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/profile")
    public Result<User> getProfile() {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getProfile(userId);
        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result<String> updateProfile(@RequestBody User user) {
        user.setId(BaseContext.getCurrentId());
        userService.updateProfile(user);
        return Result.success("修改成功");
    }

    @GetMapping("/messages")
    public Result<PageResult> getMessages(Page<UserMessage> page) {
        Long userId = BaseContext.getCurrentId();
        Page<UserMessage> pageResult = messageService.getMessages(userId, page);
        PageResult result = new PageResult(pageResult.getTotal(), pageResult.getRecords());
        return Result.success(result);
    }

    @GetMapping("/messages/{id}")
    public Result<UserMessage> getMessageDetail(@PathVariable Long id) {
        Long userId = BaseContext.getCurrentId();
        UserMessage message = messageService.getMessageDetail(userId, id);
        return Result.success(message);
    }

    @GetMapping("/notifications")
    public Result<PageResult> getNotifications(Page<UserMessage> page) {
        Page<UserMessage> pageResult = messageService.getNotifications(page);
        PageResult result = new PageResult(pageResult.getTotal(), pageResult.getRecords());
        return Result.success(result);
    }
}