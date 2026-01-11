package com.school_enterprise_platform.controller.message;

import com.school_enterprise_platform.result.Result;
import com.school_enterprise_platform.service.MessageService;
import com.school_enterprise_platform.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息控制器（支持用户间实时聊天 + 系统通知）
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发送私信（用户间实时通信）
     * @param toUserId 接收者ID
     * @param content 消息内容
     */
    @PostMapping("/send")
    public Result<String> sendPrivateMessage(
            @RequestParam Long toUserId,
            @RequestParam String content) {
        Long fromUserId = BaseContext.getCurrentId();
        if (fromUserId.equals(toUserId)) {
            return Result.error("不能给自己发消息");
        }
        if (content == null || content.trim().isEmpty()) {
            return Result.error("消息内容不能为空");
        }

        messageService.sendPrivateMessage(fromUserId, toUserId, content);
        return Result.success("发送成功");
    }
}