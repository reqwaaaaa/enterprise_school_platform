package com.school_enterprise_platform.service;

import com.school_enterprise_platform.dto.SeekerStatisticsDTO;

public interface SeekerStatisticsService {
    SeekerStatisticsDTO getStatistics(Long userId);
}