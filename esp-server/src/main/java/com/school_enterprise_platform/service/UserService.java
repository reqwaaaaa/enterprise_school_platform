package com.school_enterprise_platform.service;

import com.school_enterprise_platform.dto.RegisterDTO;
import com.school_enterprise_platform.dto.LoginDTO;
import com.school_enterprise_platform.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    User login(LoginDTO loginDTO);

    /**
     * 用户注册
     */
    void register(RegisterDTO registerDTO);
    /**
     * 查看个人信息
     */
    User getProfile(Long userId);

    /**
     * 修改个人信息
     */
    void updateProfile(User user);
}