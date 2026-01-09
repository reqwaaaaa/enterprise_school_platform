package com.school_enterprise_platform.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 注册请求 DTO
 * 包含所有角色可能需要的字段，前端根据选择的角色类型决定哪些字段必填
 */
@Data
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;  // 可选

    @NotNull(message = "用户角色不能为空")
    private Byte userRole;  // 1-6

    // 通用字段（所有角色可选）
    private String realName;     // 真实姓名（多数角色需要）
    private Byte gender;         // 性别：1男 2女

    // 求职者专用（userRole=1）
    private java.time.LocalDate birthDate;
    private String education;    // 学历
    private Integer workExperience;  // 工作经验年限

    // 在校学生专用（userRole=2）
    private String studentNumber;   // 学号
    private Long schoolId;          // 学校ID（必填）
    private String major;           // 专业
    private String className;       // 班级
    private Integer enrollmentYear; // 入学年份
    private Long counselorId;       // 辅导员ID（可选）

    // 企业HR专用（userRole=3）
    private Long enterpriseId;      // 企业ID（必填）
    private String department;      // 部门
    private String position;        // 职位

    // 辅导员/讲师专用（userRole=4/5）
    private String title;           // 职称（讲师使用）

    // 政府管理员专用（userRole=6）
    private String governmentRole;  // 角色权限描述（如 "审核员"）

    // 注意：前端注册时，根据选择的 userRole 动态显示对应字段
    // 后端不做字段必填校验（太复杂），由前端控制或后续完善
}