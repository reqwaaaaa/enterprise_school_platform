package com.school_enterprise_platform.mapper;

import com.school_enterprise_platform.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户基础信息表，存储平台所有用户的基本信息 Mapper 接口
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE username = #{username} AND is_deleted = 0")
    User getByUsername(String username);
}
