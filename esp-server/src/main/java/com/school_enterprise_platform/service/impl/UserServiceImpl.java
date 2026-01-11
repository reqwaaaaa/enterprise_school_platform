package com.school_enterprise_platform.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.dto.LoginDTO;
import com.school_enterprise_platform.dto.RegisterDTO;
import com.school_enterprise_platform.entity.*;
import com.school_enterprise_platform.exception.AccountLockedException;
import com.school_enterprise_platform.exception.AccountNotFoundException;
import com.school_enterprise_platform.exception.PasswordErrorException;
import com.school_enterprise_platform.mapper.*;
import com.school_enterprise_platform.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 * 功能：统一登录校验 + 用户注册 + 个人信息查看/修改（公共模块）
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MessageService messageService;

    // 角色子表 Mapper
    @Autowired
    private SocialUserMapper socialUserMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private HrMapper hrMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private TrainerMapper trainerMapper;

    @Autowired
    private GovernmentAdminMapper governmentAdminMapper;

    // ====================== 登录 ======================
    @Override
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new AccountNotFoundException("账号不存在");
        }

        String salt = user.getSalt() == null ? "" : user.getSalt();
        String encryptedPassword = DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));

        log.info("前端传密码: {}", password);
        log.info("数据库盐值: {}", salt);
        log.info("后端计算MD5: {}", encryptedPassword);
        log.info("数据库存储MD5: {}", user.getPassword());

        if (!encryptedPassword.equals(user.getPassword())) {
            throw new PasswordErrorException("密码错误");
        }

        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new AccountLockedException("账号被锁定");
        }

        return user;
    }

    // ====================== 注册 ======================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO dto) {
        // 1. 唯一性校验
        if (this.lambdaQuery().eq(User::getUsername, dto.getUsername()).exists()) {
            throw new RuntimeException("用户名已存在");
        }
        if (this.lambdaQuery().eq(User::getEmail, dto.getEmail()).exists()) {
            throw new RuntimeException("邮箱已存在");
        }
        if (StringUtils.hasText(dto.getPhone()) && this.lambdaQuery().eq(User::getPhone, dto.getPhone()).exists()) {
            throw new RuntimeException("手机号已存在");
        }

        // 2. 密码加密
        String salt = RandomUtil.randomString(16);
        String encryptedPassword = DigestUtils.md5DigestAsHex((dto.getPassword() + salt).getBytes(StandardCharsets.UTF_8));

        // 3. 插入 user 主表
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setUserRole(dto.getUserRole());
        user.setStatus((byte) 1);
        user.setCreateTime(LocalDateTime.now());
        this.save(user);  // 自动回填 id

        // 4. 插入对应角色子表
        switch (dto.getUserRole()) {
            case 1: // 求职者
                SocialUser socialUser = new SocialUser();
                socialUser.setUserId(user.getId());
                socialUser.setRealName(dto.getRealName());
                socialUser.setGender(dto.getGender());
                socialUser.setBirthDate(dto.getBirthDate());
                socialUser.setEducation(dto.getEducation());
                socialUser.setWorkExperience(dto.getWorkExperience());
                socialUserMapper.insert(socialUser);
                break;

            case 2: // 在校学生
                Student student = new Student();
                student.setUserId(user.getId());
                student.setStudentNumber(dto.getStudentNumber());
                student.setRealName(dto.getRealName());
                student.setGender(dto.getGender());
                student.setSchoolId(dto.getSchoolId());
                student.setMajor(dto.getMajor());
                student.setClassName(dto.getClassName());
                student.setEnrollmentYear(dto.getEnrollmentYear());
                student.setCounselorId(dto.getCounselorId());
                studentMapper.insert(student);
                break;

            case 3: // 企业HR
                Hr hr = new Hr();
                hr.setUserId(user.getId());
                hr.setEnterpriseId(dto.getEnterpriseId());
                hr.setRealName(dto.getRealName());
                hr.setDepartment(dto.getDepartment());
                hr.setPosition(dto.getPosition());
                hrMapper.insert(hr);
                break;

            case 4: // 辅导员
                Counselor counselor = new Counselor();
                counselor.setUserId(user.getId());
                counselor.setName(dto.getRealName());
                counselor.setSchoolId(dto.getSchoolId());
                counselorMapper.insert(counselor);
                break;

            case 5: // 培训讲师
                Trainer trainer = new Trainer();
                trainer.setUserId(user.getId());
                trainer.setSchoolId(dto.getSchoolId());
                trainer.setRealName(dto.getRealName());
                trainer.setTitle(dto.getTitle());
                trainerMapper.insert(trainer);
                break;

            case 6: // 政府管理员
                GovernmentAdmin governmentAdmin = new GovernmentAdmin();
                governmentAdmin.setUserId(user.getId());
                governmentAdmin.setDepartment(dto.getDepartment());
                governmentAdmin.setRole(dto.getGovernmentRole());
                governmentAdmin.setStatus((byte) 1);
                governmentAdminMapper.insert(governmentAdmin);
                break;

            default:
                throw new RuntimeException("不支持的用户角色类型");
        }
    }

    // ====================== 公共模块：个人信息查看 ======================
    @Override
    public User getProfile(Long userId) {
        String key = "common:profile:" + userId;
        User user = (User) redisTemplate.opsForValue().get(key);

        if (user == null) {
            user = this.getById(userId);
            if (user != null) {
                redisTemplate.opsForValue().set(key, user, Duration.ofHours(2));
            }
        }

        if (user != null) {
            user.setPassword(null);
            user.setSalt(null);
        }
        return user;
    }

    // ====================== 公共模块：个人信息修改 ======================
    @Override
    public void updateProfile(User updateUser) {
        User user = new User();
        user.setId(updateUser.getId());
        user.setEmail(updateUser.getEmail());
        user.setPhone(updateUser.getPhone());
        user.setAvatar(updateUser.getAvatar());
        user.setRemark(updateUser.getRemark());

        this.updateById(user);

        String key = "common:profile:" + updateUser.getId();
        redisTemplate.delete(key);
    }

    // ==================== 公告广播 ====================
    @Override
    public void publishBroadcastMessage(String content) {
        List<User> users = this.list();
        for (User user : users) {
            UserMessage message = new UserMessage();
            message.setSenderId(0L);  // 系统发送
            message.setReceiverId(user.getId());
            message.setMessageType((byte) 3);
            message.setContent(content);
            message.setSendTime(LocalDateTime.now());
            message.setStatus((byte) 0);
            messageService.save(message);  // 使用注入的实例调用
        }
    }

    // ==================== 删除消息 ====================
    @Override
    public void removeMessageById(Long messageId) {
        messageService.removeById(messageId);  // 使用注入的实例调用
    }

    // ==================== 禁用用户 ====================
    @Override
    public void disableUser(Long id) {
        User user = this.getById(id);
        if (user != null) {
            user.setStatus((byte) 0);
            this.updateById(user);
        }
    }
}