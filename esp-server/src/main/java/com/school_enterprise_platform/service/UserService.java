package com.school_enterprise_platform.service;

import com.school_enterprise_platform.dto.LoginDTO;
import com.school_enterprise_platform.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    User login(LoginDTO loginDTO);
}