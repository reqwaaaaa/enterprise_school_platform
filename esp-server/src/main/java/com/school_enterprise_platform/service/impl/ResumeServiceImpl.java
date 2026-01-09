package com.school_enterprise_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school_enterprise_platform.entity.Resume;
import com.school_enterprise_platform.mapper.ResumeMapper;
import com.school_enterprise_platform.result.PageResult;
import com.school_enterprise_platform.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements ResumeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResult getMyResumes(Long userId, Page<Resume> page) {
        String key = "seeker:resume:list:" + userId + ":" + page.getCurrent();
        Page<Resume> cached = (Page<Resume>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return new PageResult(cached.getTotal(), cached.getRecords());
        }

        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resume::getUserId, userId)
                .orderByDesc(Resume::getId);

        Page<Resume> result = this.page(page, wrapper);
        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(30));

        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Override
    public Resume getResumeDetail(Long userId, Long resumeId) {
        String key = "seeker:resume:detail:" + resumeId;
        Resume resume = (Resume) redisTemplate.opsForValue().get(key);
        if (resume == null) {
            resume = this.getById(resumeId);
            if (resume != null && resume.getUserId().equals(userId)) {
                redisTemplate.opsForValue().set(key, resume, Duration.ofHours(1));
            } else {
                throw new RuntimeException("简历不存在或无权限");
            }
        }
        return resume;
    }

    @Override
    public void addResume(Long userId, Resume resume) {
        resume.setUserId(userId);
        this.save(resume);
        redisTemplate.delete("seeker:resume:list:" + userId + ":*");
    }

    @Override
    public void updateResume(Long userId, Resume resume) {
        Resume existing = this.getById(resume.getId());
        if (existing == null || !existing.getUserId().equals(userId)) {
            throw new RuntimeException("简历不存在或无权限");
        }
        this.updateById(resume);
        redisTemplate.delete("seeker:resume:detail:" + resume.getId());
        redisTemplate.delete("seeker:resume:list:" + userId + ":*");
    }

    @Override
    public void deleteResume(Long userId, Long resumeId) {
        Resume resume = this.getById(resumeId);
        if (resume == null || !resume.getUserId().equals(userId)) {
            throw new RuntimeException("简历不存在或无权限");
        }
        this.removeById(resumeId);
        redisTemplate.delete("seeker:resume:detail:" + resumeId);
        redisTemplate.delete("seeker:resume:list:" + userId + ":*");
    }
}