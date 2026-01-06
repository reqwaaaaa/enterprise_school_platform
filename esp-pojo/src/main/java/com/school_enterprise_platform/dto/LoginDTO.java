// 文件：esp-pojo/src/main/java/com/school_enterprise_platform/dto/LoginDTO.java
package com.school_enterprise_platform.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}