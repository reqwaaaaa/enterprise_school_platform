import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MyBatis-Plus 代码生成器
 * 已适配：
 * - 多模块结构（Entity 输出到 esp-pojo，Mapper/XML/Service/Controller 输出到 esp-server）
 * - 数据库连接安全参数（allowPublicKeyRetrieval=true）
 * - Lombok + Swagger + 逻辑删除
 * - 所有 25 张表
 */
public class codegenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/school_enterprise_platform?" +
                "useUnicode=true&characterEncoding=utf8&" +
                "zeroDateTimeBehavior=convertToNull&" +
                "useSSL=false&" +
                "serverTimezone=Asia/Shanghai&" +
                "allowPublicKeyRetrieval=true";

        String username = "root";
        String password = "#020728Ceq";

        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("Naiweilanlan")
                            .enableSwagger()                       // 开启 Swagger 注解（如 @ApiModel）
                            .dateType(DateType.TIME_PACK)          // 使用 java.time 包（LocalDateTime）
                            .commentDate("yyyy-MM-dd")             // 注释日期格式
                            .outputDir("esp-pojo/src/main/java");  // Entity 默认输出目录
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.school_enterprise_platform")  // 父包名
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller");

                    // 自定义输出路径（关键！多模块结构必须这样配置）
                    Map<OutputFile, String> pathInfo = new HashMap<>();
                    pathInfo.put(OutputFile.entity,       "esp-pojo/src/main/java/com/school_enterprise_platform/entity");
                    pathInfo.put(OutputFile.mapper,       "esp-server/src/main/java/com/school_enterprise_platform/mapper");
                    pathInfo.put(OutputFile.xml,          "esp-server/src/main/resources/mapper");
                    pathInfo.put(OutputFile.service,      "esp-server/src/main/java/com/school_enterprise_platform/service");
                    pathInfo.put(OutputFile.serviceImpl,  "esp-server/src/main/java/com/school_enterprise_platform/service/impl");
                    pathInfo.put(OutputFile.controller,   "esp-server/src/main/java/com/school_enterprise_platform/controller");
                    builder.pathInfo(pathInfo);
                })
                // 策略配置
                .strategyConfig(builder -> {
                    // 指定需要生成的表（所有 25 张表）
                    builder.addInclude(
                                    "user", "school", "enterprise", "student", "counselor", "trainer",
                                    "social_user", "hr", "government_admin", "course", "resume",
                                    "job_position", "job_application", "declaration", "exam", "exam_record",
                                    "course_chapter", "learning_record", "course_enrollment",
                                    "system_log", "user_message", "tag", "job_tag", "user_tag_profile", "student_analysis"
                            )
                            // Entity 配置
                            .entityBuilder()
                            .enableLombok()                     // 开启 Lombok（@Data 等）
                            .enableTableFieldAnnotation()       // 字段注解（如 @TableField）
                            .logicDeleteColumnName("is_deleted") // 逻辑删除字段（如果表中有）
                            .enableFileOverride()               // 允许覆盖已生成文件（谨慎使用）
                            // Mapper 配置
                            .mapperBuilder()
                            .enableMapperAnnotation()           // @Mapper 注解
                            .enableBaseResultMap()              // 开启 resultMap
                            .enableBaseColumnList()             // 开启 baseColumnList
                            .enableFileOverride()
                            // Service 配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .enableFileOverride()
                            // Controller 配置
                            .controllerBuilder()
                            .enableRestStyle()                  // REST 风格（@RestController）
                            .enableFileOverride();
                })
                // 模板引擎
                .templateEngine(new FreemarkerTemplateEngine())
                // 执行生成
                .execute();
    }
}