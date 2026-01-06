package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户消息表，存储用户间的沟通记录和系统通知
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("user_message")
@ApiModel(value = "UserMessage对象", description = "用户消息表，存储用户间的沟通记录和系统通知")
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("发送者ID")
    @TableField("sender_id")
    private Long senderId;

    @ApiModelProperty("接收者ID")
    @TableField("receiver_id")
    private Long receiverId;

    @ApiModelProperty("消息类型：1私信 2课程通知 3系统通知")
    @TableField("message_type")
    private Byte messageType;

    @ApiModelProperty("消息内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("发送时间")
    @TableField("send_time")
    private LocalDateTime sendTime;

    @ApiModelProperty("已读状态：0未读 1已读")
    @TableField("status")
    private Byte status;
}
