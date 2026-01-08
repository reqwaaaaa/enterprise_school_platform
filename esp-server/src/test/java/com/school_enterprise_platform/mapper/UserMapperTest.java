// 文件路径：esp-server/src/test/java/com/school_enterprise_platform/mapper/UserDataInitTest.java

package com.school_enterprise_platform.mapper;

import com.school_enterprise_platform.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 完全使用 Mapper 自动完成：
 * 1. 删除所有现有测试账号
 * 2. 动态计算 MD5（不硬编码加密密码）
 * 3. 插入新的标准测试账号（序号从 01 开始）
 *
 * 优势：
 * - 纯 Java 代码 + MyBatis-Plus Mapper 操作，无需手动执行 SQL
 * - 密码动态加密，保证与登录逻辑 100% 一致
 * - 真实写入数据库（用于后续登录接口测试）
 * - 执行一次即可完成所有数据初始化
 */
@SpringBootTest
// @Transactional  // ← 必须注释掉，否则插入后会回滚
@Slf4j
class UserDataInitTest {

    @Autowired
    private UserMapper userMapper;

    private static final String PLAIN_PASSWORD = "123456";  // 所有测试账号统一明文密码
    private static final String SALT = "abc";               // 统一盐值

    /**
     * 动态计算 MD5：明文密码 + 盐
     */
    private String encryptPassword(String password, String salt) {
        return DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void initTestUsers() {
        String encryptedPassword = encryptPassword(PLAIN_PASSWORD, SALT);

        // 定义要初始化的测试账号（从 01 开始编号）
        List<User> users = Arrays.asList(
                createUser("Naiweilanlan", "测试主账号", (byte) 1),
                createUser("seeker01", "求职者01", (byte) 1),
                createUser("student01", "学生01", (byte) 2),
                createUser("hr01", "企业HR01", (byte) 3),
                createUser("counselor01", "辅导员01", (byte) 4),
                createUser("teacher01", "讲师01", (byte) 5),
                createUser("admin01", "政府管理员01", (byte) 6)
        );

        // 删除所有可能存在的旧测试账号（避免冲突）
        List<String> usernames = users.stream()
                .map(User::getUsername)
                .toList();

        int deleted = userMapper.delete(
                new LambdaQueryWrapper<User>()
                        .in(User::getUsername, usernames)
        );
        log.info("删除旧测试账号 {} 条", deleted);

        // 插入新测试账号
        for (User user : users) {
            user.setPassword(encryptedPassword);
            user.setSalt(SALT);
            user.setCreateTime(LocalDateTime.now());
            user.setStatus((byte) 1);

            int rows = userMapper.insert(user);
            assertThat(rows).isEqualTo(1);

            log.info("插入成功 → 用户名: {}, 角色: {}, ID: {}",
                    user.getUsername(), (int) user.getUserRole(), user.getId());
        }

        log.info("=== 测试账号初始化完成 ===");
        log.info("所有账号密码明文统一为: {}", PLAIN_PASSWORD);
        log.info("可立即使用任意账号测试 /auth/login 接口");
    }

    /**
     * 创建 User 对象（基础字段）
     */
    private User createUser(String username, String remark, byte role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@Test.edu.com");
        user.setPhone("13816030769");
        user.setUserRole(role);
        user.setRemark(remark);
        return user;
    }
}