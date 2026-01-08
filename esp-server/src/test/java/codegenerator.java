import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MyBatis-Plus 代码生成器 —— 仅生成 Entity 类
 * 适用于多模块项目：Entity 输出到 esp-pojo 模块
 * 已开启 Lombok + OpenAPI 3 注解（@Schema）+ 逻辑删除支持
 */
public class codegenerator {

    public static void main(String[] args) {
        // 数据库连接（已包含 allowPublicKeyRetrieval=true，解决连接问题）
        String url = "jdbc:mysql://localhost:3306/school_enterprise_platform?" +
                "useUnicode=true&characterEncoding=utf8&" +
                "zeroDateTimeBehavior=convertToNull&" +
                "useSSL=false&" +
                "serverTimezone=Asia/Shanghai&" +
                "allowPublicKeyRetrieval=true";

        String username = "root";
        String password = "#020728Ceq";  // 请根据实际情况修改

        FastAutoGenerator.create(url, username, password)
                // ==================== 全局配置 ====================
                .globalConfig(builder -> {
                    builder.author("Naiweilanlan")                  // 作者名
                            .dateType(DateType.TIME_PACK)           // 使用 java.time.LocalDateTime
                            .commentDate("yyyy-MM-dd")              // 注释日期格式
                            .disableOpenDir()                       // 生成后不打开目录
                            .outputDir("esp-pojo/src/main/java");   // Entity 输出基础路径
                })
                // ==================== 包配置 ====================
                .packageConfig(builder -> {
                    builder.parent("com.school_enterprise_platform")  // 父包名
                            .entity("entity");                        // Entity 子包

                    // 仅指定 Entity 输出路径（其他路径不设置，即不生成其他文件）
                    Map<OutputFile, String> pathInfo = new HashMap<>();
                    pathInfo.put(OutputFile.entity,
                            "esp-pojo/src/main/java/com/school_enterprise_platform/entity");
                    builder.pathInfo(pathInfo);
                })
                // ==================== 策略配置（只生成 Entity） ====================
                .strategyConfig(builder -> {
                    // 指定所有 25 张表
                    builder.addInclude(
                                    "user", "school", "enterprise", "student", "counselor", "trainer",
                                    "social_user", "hr", "government_admin", "course", "resume",
                                    "job_position", "job_application", "declaration", "exam", "exam_record",
                                    "course_chapter", "learning_record", "course_enrollment",
                                    "system_log", "user_message", "tag", "job_tag", "user_tag_profile", "student_analysis"
                            )
                            // Entity 策略（重点配置）
                            .entityBuilder()
                            .enableLombok()                              // 开启 Lombok (@Data)
                            .enableTableFieldAnnotation()                // 字段注解 (@TableField)
                            .addTableFills()                              // 可添加创建/更新时间填充（若需要）
                            .logicDeleteColumnName("is_deleted")         // 逻辑删除字段（若表中有）
                            .enableFileOverride()                        // 允许覆盖已存在的 Entity（谨慎）
                            .formatFileName("%s")                        // 文件名格式：User（不加 Entity 后缀）
                            // 关闭其他模块生成（关键！只生成 Entity）
                            .mapperBuilder().disable()
                            .serviceBuilder().disable()
                            .controllerBuilder().disable();
                })
                // ==================== 模板引擎 ====================
                .templateEngine(new FreemarkerTemplateEngine())
                // ==================== 执行生成 ====================
                .execute();
    }
}