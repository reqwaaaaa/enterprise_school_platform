package com.school_enterprise_platform.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class UploadService {

    @Autowired
    private COSClient cosClient;

    @Value("${tencent.cos.bucket}")
    private String bucket;

    @Value("${tencent.cos.base-url}")
    private String baseUrl;

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件为空");
        }

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 上传到 COS
        File tempFile = new File(fileName);
        try {
            file.transferTo(tempFile);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, tempFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return baseUrl + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("上传失败");
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}