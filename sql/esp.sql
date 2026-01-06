CREATE DATABASE IF NOT EXISTS school_enterprise_platform
CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE school_enterprise_platform;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. user 用户基础信息表（所有角色共用基础表，最先创建）
CREATE TABLE `user` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID，自增主键，系统唯一标识',
                        `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名，登录用户名，唯一',
                        `password` VARCHAR(255) NOT NULL COMMENT '密码，加密存储（MD5+盐加密）',
                        `email` VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱，唯一邮箱',
                        `phone` VARCHAR(20) COMMENT '手机号，可用于登录/找回密码',
                        `user_role` TINYINT NOT NULL COMMENT '用户类型：1=求职者,2=学生,3=HR,4=辅导员,5=讲师,6=政府管理员',
                        `school_id` BIGINT COMMENT '所属学校ID，学生/讲师/辅导员使用',
                        `enterprise_id` BIGINT COMMENT '所属企业ID，HR/求职者使用',
                        `organization_type` TINYINT COMMENT '组织类型：1=学校,2=企业,3=政府',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                        `last_login` DATETIME COMMENT '最后登录时间',
                        `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0=禁用,1=启用',
                        `avatar` VARCHAR(255) COMMENT '头像URL',
                        `remark` TEXT COMMENT '备注，额外说明',
                        `salt` VARCHAR(50) COMMENT '密码盐值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基础信息表，存储平台所有用户的基本信息';

-- 2. school 学校表
CREATE TABLE `school` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '学校ID，自增主键',
                          `name` VARCHAR(200) COMMENT '学校名称',
                          `type` VARCHAR(50) NOT NULL COMMENT '学校类型：本科/职业/研究型等',
                          `location` VARCHAR(200) NOT NULL COMMENT '所在地：城市/地区',
                          `introduction` TEXT NOT NULL COMMENT '学校简介',
                          `create_time` DATETIME COMMENT '注册时间',
                          `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学校信息表，存储合作院校的基本信息和资质';

-- 3. enterprise 企业表
CREATE TABLE `enterprise` (
                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '企业ID，自增主键',
                              `name` VARCHAR(200) COMMENT '企业名称',
                              `credit_code` VARCHAR(50) COMMENT '统一社会信用代码，唯一',
                              `type` VARCHAR(50) NOT NULL COMMENT '企业类型：如民企/国企',
                              `location` VARCHAR(200) NOT NULL COMMENT '所在地：城市/地区',
                              `introduction` TEXT NOT NULL COMMENT '企业简介',
                              `create_time` DATETIME COMMENT '注册时间',
                              `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业信息表，存储合作企业的基本信息和认证状态';

-- 4. student 在校学生表（依赖user、school、counselor）
CREATE TABLE `student` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '学生ID，自增主键',
                           `user_id` BIGINT NOT NULL COMMENT '关联user表',
                           `student_number` VARCHAR(20) NOT NULL COMMENT '学号',
                           `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
                           `gender` TINYINT COMMENT '性别：1=男,2=女',
                           `school_id` BIGINT NOT NULL COMMENT '学校ID',
                           `major` VARCHAR(100) NOT NULL COMMENT '专业',
                           `class_name` VARCHAR(50) COMMENT '班级',
                           `enrollment_year` INT NOT NULL COMMENT '入学年份',
                           `counselor_id` BIGINT COMMENT '辅导员ID',
                           FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                           FOREIGN KEY (`school_id`) REFERENCES `school`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在校学生信息表，存储学生的学籍信息和学业相关信息';

-- 5. counselor 辅导员表（依赖user、school）
CREATE TABLE `counselor` (
                             `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '辅导员ID，自增主键',
                             `user_id` BIGINT NOT NULL COMMENT '关联user表',
                             `name` VARCHAR(50) NOT NULL COMMENT '姓名',
                             `school_id` BIGINT NOT NULL COMMENT '学校ID',
                             FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                             FOREIGN KEY (`school_id`) REFERENCES `school`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='辅导员信息表，存储高校辅导员的基本信息和所带班级信息';

-- 6. trainer 培训讲师表（依赖user、school）
CREATE TABLE `trainer` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '讲师ID，自增主键',
                           `user_id` BIGINT NOT NULL COMMENT '关联user表',
                           `school_id` BIGINT NOT NULL COMMENT '学校ID',
                           `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
                           `title` VARCHAR(50) COMMENT '职称',
                           FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                           FOREIGN KEY (`school_id`) REFERENCES `school`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训讲师信息表，存储讲师的基本信息和教学资质';

-- 7. social_user 社会个人用户表（依赖user）
CREATE TABLE `social_user` (
                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '社会用户ID，自增主键',
                               `user_id` BIGINT NOT NULL COMMENT '关联user表',
                               `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
                               `gender` TINYINT COMMENT '性别：1=男,2=女',
                               `birth_date` DATE COMMENT '出生日期',
                               `education` VARCHAR(50) COMMENT '学历',
                               `work_experience` INT COMMENT '工作经验年限',
                               FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社会个人用户表，存储社会求职者的详细个人信息';

-- 8. hr 企业HR表（依赖user、enterprise）
CREATE TABLE `hr` (
                      `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'HR ID，自增主键',
                      `user_id` BIGINT NOT NULL COMMENT '关联user表',
                      `enterprise_id` BIGINT NOT NULL COMMENT '企业ID',
                      `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
                      `department` VARCHAR(100) COMMENT '部门',
                      `position` VARCHAR(50) COMMENT '职位',
                      FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                      FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业HR信息表，存储企业招聘负责人的职位和部门信息';

-- 9. government_admin 政府管理员表（依赖user）
CREATE TABLE `government_admin` (
                                    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID，自增主键',
                                    `user_id` BIGINT NOT NULL COMMENT '关联user表',
                                    `department` VARCHAR(100) COMMENT '所属部门，如“郫都区人社局”',
                                    `role` VARCHAR(50) NOT NULL COMMENT '角色权限：1审核员 2统计员 3系统运维等',
                                    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
                                    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='政府管理员表，存储政府工作人员的角色权限信息';

-- 10. course 课程表（依赖trainer、school）
CREATE TABLE `course` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID，自增主键',
                          `name` VARCHAR(200) NOT NULL COMMENT '课程名称',
                          `type` TINYINT NOT NULL COMMENT '课程类型：1公共课程 2本校课程',
                          `trainer_id` BIGINT NOT NULL COMMENT '讲师ID',
                          `school_id` BIGINT COMMENT '学校ID（本校课程使用）',
                          `tags` VARCHAR(200) COMMENT '标签，多标签逗号分隔',
                          `introduction` TEXT COMMENT '课程简介',
                          `materials` TEXT COMMENT '课件文件路径，支持多个',
                          `duration` INT COMMENT '学习时长，单位：小时',
                          `start_time` DATETIME NOT NULL COMMENT '开课时间',
                          `end_time` DATETIME COMMENT '结课时间',
                          `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0未发布 1已发布 2已结束',
                          FOREIGN KEY (`trainer_id`) REFERENCES `trainer`(`id`) ON DELETE RESTRICT,
                          FOREIGN KEY (`school_id`) REFERENCES `school`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程信息表，存储平台所有课程的基本信息和教学资源';

-- 11. resume 简历表（依赖user）
CREATE TABLE `resume` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '简历ID，自增主键',
                          `user_id` BIGINT NOT NULL COMMENT '用户ID（社会个人用户或学生）',
                          `version` INT NOT NULL COMMENT '简历版本',
                          `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
                          `gender` TINYINT COMMENT '性别：1男 2女',
                          `birth_date` DATE COMMENT '出生日期',
                          `education` VARCHAR(50) COMMENT '学历',
                          FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历信息表，存储用户的多版本简历内容和求职信息';

-- 12. job_position 职位表（依赖enterprise）
CREATE TABLE `job_position` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '职位ID，自增主键',
                                `enterprise_id` BIGINT NOT NULL COMMENT '企业ID',
                                `title` VARCHAR(100) NOT NULL COMMENT '职位名称',
                                `type` TINYINT NOT NULL COMMENT '职位类型：1全职 2实习 3兼职',
                                `location` VARCHAR(200) COMMENT '工作地点',
                                `salary` VARCHAR(50) COMMENT '薪资范围',
                                `requirements` TEXT COMMENT '岗位要求',
                                `headcount` INT COMMENT '招聘人数',
                                `publish_time` DATETIME NOT NULL COMMENT '发布时间',
                                `deadline` DATETIME COMMENT '截止时间',
                                `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0草稿 1发布 2已结束',
                                FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位信息表，存储企业发布的招聘职位详情和要求';

-- 13. job_application 职位申请表（依赖user、job_position、resume）
CREATE TABLE `job_application` (
                                   `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID，自增主键',
                                   `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                   `job_id` BIGINT NOT NULL COMMENT '职位ID',
                                   `resume_id` BIGINT COMMENT '简历ID',
                                   `apply_time` DATETIME NOT NULL COMMENT '申请时间',
                                   `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0待处理 1已通过 2已拒绝',
                                   FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                                   FOREIGN KEY (`job_id`) REFERENCES `job_position`(`id`) ON DELETE RESTRICT,
                                   FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位申请表，存储用户投递简历的申请记录和状态';

-- 14. declaration 申报表（依赖enterprise、user、government_admin）
CREATE TABLE `declaration` (
                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申报ID，自增主键',
                               `enterprise_id` BIGINT NOT NULL COMMENT '企业ID',
                               `user_id` BIGINT COMMENT '用户ID（学员）',
                               `type` TINYINT NOT NULL COMMENT '申报类型：1高技能培训 2技能大师工作室等',
                               `amount` DECIMAL(10,2) NOT NULL COMMENT '申报金额',
                               `apply_time` DATETIME NOT NULL COMMENT '申报时间',
                               `status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态：0待审核 1通过 2不通过 3候补',
                               `reviewer_id` BIGINT COMMENT '审核人ID',
                               FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise`(`id`) ON DELETE RESTRICT,
                               FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                               FOREIGN KEY (`reviewer_id`) REFERENCES `government_admin`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='申报表，存储企业或用户的补贴申报信息和审核流程';

-- 15. exam 考试表（依赖course、trainer）
CREATE TABLE `exam` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '考试ID，自增主键',
                        `name` VARCHAR(100) NOT NULL COMMENT '考试名称',
                        `type` TINYINT NOT NULL COMMENT '考试类型：1课程测验 2认证考试 3公共测试',
                        `course_id` BIGINT COMMENT '课程ID',
                        `start_time` DATETIME NOT NULL COMMENT '开始时间',
                        `end_time` DATETIME COMMENT '结束时间',
                        `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0未发布 1进行中 2已结束',
                        `creator_id` BIGINT NOT NULL COMMENT '创建人ID（讲师）',
                        FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE SET NULL,
                        FOREIGN KEY (`creator_id`) REFERENCES `trainer`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试信息表，存储各类考试的基本信息和时间安排';

-- 16. exam_record 考试记录表（依赖user、exam）
CREATE TABLE `exam_record` (
                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID，自增主键',
                               `user_id` BIGINT NOT NULL COMMENT '用户ID',
                               `exam_id` BIGINT NOT NULL COMMENT '考试ID',
                               `score` DECIMAL(5,2) COMMENT '得分',
                               `submit_time` DATETIME NOT NULL COMMENT '提交时间',
                               `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0未提交 1已提交 2已批阅',
                               FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                               FOREIGN KEY (`exam_id`) REFERENCES `exam`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试记录表，存储用户参加考试的成绩和提交状态';

-- 17. course_chapter 课程章节表（依赖course）
CREATE TABLE `course_chapter` (
                                  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '章节ID，自增主键',
                                  `course_id` BIGINT NOT NULL COMMENT '课程ID',
                                  `name` VARCHAR(200) NOT NULL COMMENT '章节名称',
                                  `sequence` INT NOT NULL COMMENT '章节顺序，用于排序显示',
                                  `type` TINYINT NOT NULL COMMENT '章节类型：0视频 1文档 2问答 3其他',
                                  `resource_path` VARCHAR(200) COMMENT '资源路径',
                                  FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程章节表，存储课程的章节结构和学习资源';

-- 18. learning_record 学习记录表（依赖user、course、course_chapter）
CREATE TABLE `learning_record` (
                                   `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID，自增主键',
                                   `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                   `course_id` BIGINT NOT NULL COMMENT '课程ID',
                                   `chapter_id` BIGINT NOT NULL COMMENT '章节ID',
                                   `status` TINYINT NOT NULL DEFAULT 0 COMMENT '学习状态：0未开始 1学习中 2已完成',
                                   `start_time` DATETIME COMMENT '开始学习时间',
                                   `finish_time` DATETIME COMMENT '完成时间',
                                   FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                                   FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE RESTRICT,
                                   FOREIGN KEY (`chapter_id`) REFERENCES `course_chapter`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表，存储用户的学习进度和完成状态';

-- 19. course_enrollment 课程报名表（依赖user、course）
CREATE TABLE `course_enrollment` (
                                     `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报名ID，自增主键',
                                     `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                     `course_id` BIGINT NOT NULL COMMENT '课程ID',
                                     `enrollment_time` DATETIME NOT NULL COMMENT '报名时间',
                                     `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0取消 1已报名 2完成',
                                     FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                                     FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程报名表，存储用户报名课程的记录和状态';

-- 20. system_log 系统日志表（依赖user）
CREATE TABLE `system_log` (
                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID，自增主键',
                              `user_id` BIGINT COMMENT '操作用户ID，可为空（系统操作）',
                              `action_type` VARCHAR(100) NOT NULL COMMENT '操作类型：如登录、添加课程、审批等',
                              `target` VARCHAR(100) COMMENT '操作对象',
                              `details` TEXT COMMENT '操作详细信息',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                              FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表，存储平台操作日志用于审计和监控';

-- 21. user_message 用户消息表（依赖user）
CREATE TABLE `user_message` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID，自增主键',
                                `sender_id` BIGINT NOT NULL COMMENT '发送者ID',
                                `receiver_id` BIGINT NOT NULL COMMENT '接收者ID',
                                `message_type` TINYINT NOT NULL COMMENT '消息类型：1私信 2课程通知 3系统通知',
                                `content` TEXT NOT NULL COMMENT '消息内容',
                                `send_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
                                `status` TINYINT NOT NULL DEFAULT 0 COMMENT '已读状态：0未读 1已读',
                                FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT,
                                FOREIGN KEY (`receiver_id`) REFERENCES `user`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息表，存储用户间的沟通记录和系统通知';

-- 22. tag 标签表（新增，用于课程/岗位推荐）
CREATE TABLE `tag` (
                       `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID，自增主键',
                       `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
                       `category` TINYINT NOT NULL COMMENT '类别：1技能 2行业 3岗位方向 4能力维度',
                       `description` VARCHAR(200) COMMENT '标签描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签主表，用于课程和岗位标签管理';

-- 23. job_tag 岗位标签表（依赖job_position、tag）
CREATE TABLE `job_tag` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID，自增主键',
                           `job_id` BIGINT NOT NULL COMMENT '职位ID',
                           `tag_id` BIGINT NOT NULL COMMENT '标签ID',
                           `weight` DECIMAL(3,2) DEFAULT 1.00 COMMENT '权重，默认1.0',
                           FOREIGN KEY (`job_id`) REFERENCES `job_position`(`id`) ON DELETE CASCADE,
                           FOREIGN KEY (`tag_id`) REFERENCES `tag`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位标签表，用于岗位能力匹配推荐';

-- 24. user_tag_profile 用户能力画像表（依赖user、tag）
CREATE TABLE `user_tag_profile` (
                                    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID，自增主键',
                                    `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
                                    `proficiency` TINYINT COMMENT '熟练度：1-5级',
                                    `source` TINYINT COMMENT '来源：1课程 2考试 3人工评估',
                                    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
                                    FOREIGN KEY (`tag_id`) REFERENCES `tag`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户能力画像表，用于学生/求职者能力结构分析';

-- 25. student_analysis 学生就业与学习汇总表（依赖student）
CREATE TABLE `student_analysis` (
                                    `student_id` BIGINT PRIMARY KEY COMMENT '学生ID，主键',
                                    `total_courses` INT COMMENT '总课程数',
                                    `completed_courses` INT COMMENT '已完成课程数',
                                    `avg_score` DECIMAL(5,2) COMMENT '平均分数',
                                    `job_apply_count` INT COMMENT '职位申请数',
                                    `job_success_count` INT COMMENT '成功职位数',
                                    `last_update` DATETIME COMMENT '最后更新时间',
                                    FOREIGN KEY (`student_id`) REFERENCES `student`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生就业与学习汇总统计表，用于辅导员查看班级分析';

SET FOREIGN_KEY_CHECKS = 1;
