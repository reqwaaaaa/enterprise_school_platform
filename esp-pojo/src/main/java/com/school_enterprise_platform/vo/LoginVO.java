package com.school_enterprise_platform.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {

    private Long id;              // 用户ID

    private String username;      // 用户名

    private Integer role;         // 角色类型（1=求职者,2=学生,3=HR,4=辅导员,5=讲师,6=政府管理员）

    private String token;         // JWT Token

    // 可选：额外返回信息，如用户真实姓名、头像等
    private String realName;

    private String avatar;
}