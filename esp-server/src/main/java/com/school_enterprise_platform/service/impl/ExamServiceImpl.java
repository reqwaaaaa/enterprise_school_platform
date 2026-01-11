package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.entity.Exam;
import com.school_enterprise_platform.entity.ExamRecord;
import com.school_enterprise_platform.mapper.ExamMapper;
import com.school_enterprise_platform.mapper.ExamRecordMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考试服务实现类
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord> implements ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Override
    public void submitExam(Long userId, Long examId, String answers) {
        // 校验考试是否存在且未过期
        Exam exam = examMapper.selectById(examId);
        if (exam == null || exam.getEndTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("考试不存在或已结束");
        }

        // 校验是否已提交
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getUserId, userId)
                .eq(ExamRecord::getExamId, examId);
        if (this.exists(wrapper)) {
            throw new RuntimeException("已提交过该考试");
        }

        ExamRecord record = new ExamRecord();
        record.setUserId(userId);
        record.setExamId(examId);
        record.setSubmitTime(LocalDateTime.now());
        record.setStatus((byte) 1);  // 已提交

        // 自动评分（示例：简化固定分数，实际应根据 answers 计算）
        BigDecimal score = BigDecimal.valueOf(85.5);
        record.setScore(score);

        this.save(record);
    }

    @Override
    public PageResult<Exam> getExamList(Page<Exam> page, Byte status) {
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Exam::getStatus, status);
        }
        // 改用 id 降序（数据库自增主键，最新创建的排前面）
        wrapper.orderByDesc(Exam::getId);

        Page<Exam> result = examMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }

    @Override
    public PageResult<ExamRecord> getUserExamRecords(Long userId, Page<ExamRecord> page) {
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getUserId, userId)
                .orderByDesc(ExamRecord::getSubmitTime);

        Page<ExamRecord> result = this.page(page, wrapper);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }
}