package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.dto.LoginDTO;
import com.school_enterprise_platform.entity.User;
import com.school_enterprise_platform.exception.*;
import com.school_enterprise_platform.mapper.UserMapper;
import com.school_enterprise_platform.service.UserService;
import com.school_enterprise_platform.utils.MD5Util; // 假设有 MD5 + 盐工具类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        User user = userMapper.getByUsername(username);

        if (user == null) {
            throw new AccountNotFoundException("账号不存在");
        }

        // 密码校验（MD5 + 盐）
        String encrypted = MD5Util.md5WithSalt(password, user.getSalt());
        if (!encrypted.equals(user.getPassword())) {
            throw new PasswordErrorException("密码错误");
        }

        if (user.getStatus() == 0) {
            throw new AccountLockedException("账号被锁定");
        }

        return user;
    }
}