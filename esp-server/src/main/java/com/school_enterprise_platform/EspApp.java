package com.school_enterprise_platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication  // 自动扫描本包及所有子包
@MapperScan("com.school_enterprise_platform.mapper")
@Slf4j
public class EspApp {
    public static void main(String[] args) {
        SpringApplication.run(EspApp.class, args);
        log.info("--- App started!!! ---");
    }
}