package com.school_enterprise_platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school_enterprise_platform.entity.SocialUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 社会个人用户（求职者）Mapper 接口
 */
@Mapper
public interface SocialUserMapper extends BaseMapper<SocialUser> {
}