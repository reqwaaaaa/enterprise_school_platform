package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school_enterprise_platform.entity.UserMessage;

/**
 * 用户消息服务接口
 */
public interface MessageService extends IService<UserMessage> {

    /**
     * 获取当前用户消息列表（私信 + 课程通知）
     */
    Page<UserMessage> getMessages(Long receiverId, Page<UserMessage> page);

    /**
     * 获取消息详情并标记已读
     */
    UserMessage getMessageDetail(Long receiverId, Long messageId);

    /**
     * 获取系统通知列表（全局广播）
     */
    Page<UserMessage> getNotifications(Page<UserMessage> page);

    /**
     * 发送私信（保存数据库 + 实时推送）
     */
    void sendPrivateMessage(Long fromUserId, Long toUserId, String content);
}