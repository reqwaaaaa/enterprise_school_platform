package com.school_enterprise_platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school_enterprise_platform.entity.School;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchoolMapper extends BaseMapper<School> {
}