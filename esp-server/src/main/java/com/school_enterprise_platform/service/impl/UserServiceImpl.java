package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.dto.LoginDTO;
import com.school_enterprise_platform.entity.User;
import com.school_enterprise_platform.exception.AccountLockedException;
import com.school_enterprise_platform.exception.AccountNotFoundException;
import com.school_enterprise_platform.exception.PasswordErrorException;
import com.school_enterprise_platform.mapper.UserMapper;
import com.school_enterprise_platform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.nio.charset.StandardCharsets;

/**
 * 用户服务实现类
 * 功能：统一登录校验（支持所有角色）
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        // 1. 根据用户名查询用户（MyBatis-Plus 自动处理逻辑删除）
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        // 2. 账号不存在
        if (user == null) {
            throw new AccountNotFoundException("账号不存在");
        }

        // 3. 密码校验（MD5 + 盐，安全处理 null 盐值）
        String salt = user.getSalt() == null ? "" : user.getSalt();
        String encryptedPassword = DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
        // 添加这三行日志
        log.info("前端传密码: {}", password);
        log.info("数据库盐值: {}", salt);
        log.info("后端计算MD5: {}", encryptedPassword);
        log.info("数据库存储MD5: {}", user.getPassword());

        if (!encryptedPassword.equals(user.getPassword())) {
            throw new PasswordErrorException("密码错误");
        }

        // 4. 账号状态检查
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new AccountLockedException("账号被锁定");
        }

        // 5. 可选：更新最后登录时间（生产环境推荐）
        // user.setLastLogin(LocalDateTime.now());
        // userMapper.updateById(user);

        return user;
    }
}