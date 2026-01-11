package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.entity.SystemLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school_enterprise_platform.result.PageResult;

public interface SystemLogService extends IService<SystemLog> {
    PageResult getLogs(Page<SystemLog> page, Long userId);
}