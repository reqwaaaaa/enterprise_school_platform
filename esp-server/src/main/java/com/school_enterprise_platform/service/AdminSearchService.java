package com.school_enterprise_platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school_enterprise_platform.result.PageResult;

public interface AdminSearchService {
    PageResult globalSearch(String keyword, String type, Page page);
}