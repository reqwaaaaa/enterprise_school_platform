package com.school_enterprise_platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school_enterprise_platform.entity.User;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户（推荐使用 Wrapper，避免 IDEA 误报）
     */
    default User getByUsername(String username) {
        return this.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username)
        );
    }
}