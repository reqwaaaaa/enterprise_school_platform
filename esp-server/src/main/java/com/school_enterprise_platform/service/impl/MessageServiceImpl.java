package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.entity.UserMessage;
import com.school_enterprise_platform.handler.WebSocketServer;
import com.school_enterprise_platform.mapper.MessageMapper;
import com.school_enterprise_platform.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 用户消息服务实现类
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, UserMessage> implements MessageService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public Page<UserMessage> getMessages(Long receiverId, Page<UserMessage> page) {
        String key = "common:messages:user:" + receiverId + ":page:" + page.getCurrent();
        Page<UserMessage> cached = (Page<UserMessage>) redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return cached;
        }

        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMessage::getReceiverId, receiverId)
                .ne(UserMessage::getMessageType, (byte) 3)  // 排除系统通知
                .orderByDesc(UserMessage::getSendTime);

        Page<UserMessage> result = this.page(page, wrapper);

        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(10));
        return result;
    }

    @Override
    public UserMessage getMessageDetail(Long receiverId, Long messageId) {
        UserMessage message = this.getById(messageId);

        if (message != null && message.getReceiverId().equals(receiverId) && message.getStatus() == 0) {
            message.setStatus((byte) 1);  // 标记已读
            this.updateById(message);

            // 失效该用户所有消息列表缓存
            redisTemplate.delete("common:messages:user:" + receiverId + ":*");
        }
        return message;
    }

    @Override
    public Page<UserMessage> getNotifications(Page<UserMessage> page) {
        String key = "common:notifications:page:" + page.getCurrent();
        Page<UserMessage> cached = (Page<UserMessage>) redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return cached;
        }

        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMessage::getMessageType, (byte) 3)  // 系统通知
                .orderByDesc(UserMessage::getSendTime);

        Page<UserMessage> result = this.page(page, wrapper);

        redisTemplate.opsForValue().set(key, result, Duration.ofHours(1));
        return result;
    }

    @Override
    public void sendPrivateMessage(Long fromUserId, Long toUserId, String content) {
        // 1. 保存到数据库（user_message 表）
        UserMessage message = new UserMessage();
        message.setSenderId(fromUserId);
        message.setReceiverId(toUserId);
        message.setMessageType((byte) 1);  // 1=私信
        message.setContent(content);
        message.setSendTime(LocalDateTime.now());
        message.setStatus((byte) 0);  // 未读
        this.save(message);

        // 2. 实时推送给接收者（如果在线）
        String pushContent = "来自 " + fromUserId + " 的消息：" + content;
        webSocketServer.sendMessage(toUserId.toString(), pushContent);
    }
}