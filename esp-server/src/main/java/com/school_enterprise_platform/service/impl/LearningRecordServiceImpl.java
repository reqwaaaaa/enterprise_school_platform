package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.entity.LearningRecord;
import com.school_enterprise_platform.mapper.LearningRecordMapper;
import com.school_enterprise_platform.service.LearningRecordService;
import org.springframework.stereotype.Service;

@Service
public class LearningRecordServiceImpl extends ServiceImpl<LearningRecordMapper, LearningRecord> implements LearningRecordService {
    // 如需扩展，可在此添加方法
}