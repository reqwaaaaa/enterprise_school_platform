package com.school_enterprise_platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school_enterprise_platform.entity.Enterprise;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnterpriseMapper extends BaseMapper<Enterprise> {
}