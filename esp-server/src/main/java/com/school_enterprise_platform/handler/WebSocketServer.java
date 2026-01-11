package com.school_enterprise_platform.handler;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 服务端实现
 * 支持用户实时消息推送
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{userId}")
public class WebSocketServer {

    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        sessions.put(userId, session);
        log.info("用户 {} 连接成功，Session ID: {}", userId, session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到消息: {}", message);
        // 实际项目中应解析 JSON，如 {toUserId: "xxx", content: "hello"}
        // 这里仅记录日志，业务逻辑可扩展
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId, Session session) {
        sessions.remove(userId);
        log.info("用户 {} 断开连接", userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket 发生错误", error);
    }

    /**
     * 向指定用户发送消息（管理员/系统调用）
     * @param toUserId 目标用户ID
     * @param message 消息内容
     */
    public void sendMessage(String toUserId, String message) {
        Session session = sessions.get(toUserId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
                log.info("成功发送消息给用户 {}: {}", toUserId, message);
            } catch (IOException e) {
                log.error("发送消息失败，用户 {}: {}", toUserId, e.getMessage());
                // 可选择移除失效 Session
                sessions.remove(toUserId);
            }
        } else {
            log.warn("用户 {} 不在线或 Session 已关闭，无法发送消息", toUserId);
        }
    }
}