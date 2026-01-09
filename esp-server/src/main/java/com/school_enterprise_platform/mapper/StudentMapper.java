package com.school_enterprise_platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.Student;
import com.school_enterprise_platform.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 辅导员分页查询所带学生（返回 User 信息）
     */
    Page<User> selectStudentPageByCounselor(Page<User> page, Long counselorUserId);
}