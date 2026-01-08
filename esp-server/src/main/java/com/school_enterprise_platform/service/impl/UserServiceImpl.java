package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.dto.LoginDTO;
import com.school_enterprise_platform.entity.User;
import com.school_enterprise_platform.exception.AccountLockedException;
import com.school_enterprise_platform.exception.AccountNotFoundException;
import com.school_enterprise_platform.exception.PasswordErrorException;
import com.school_enterprise_platform.mapper.UserMapper;
import com.school_enterprise_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;  // Spring 自带 MD5 工具（无盐时使用，此处结合盐）

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        // 根据用户名查询用户（数据库 user 表）
        User user = userMapper.selectOne(lambdaQuery().eq(User::getUsername, username));

        if (user == null) {
            throw new AccountNotFoundException("账号不存在");
        }

        // 密码校验：MD5 + 盐（根据数据库 salt 字段）
        String encryptedPassword = DigestUtils.md5DigestAsHex((password + user.getSalt()).getBytes());
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new PasswordErrorException("密码错误");
        }

        // 检查状态（数据库 status 字段）
        if (user.getStatus() == 0) {
            throw new AccountLockedException("账号被锁定");
        }

        return user;
    }
}