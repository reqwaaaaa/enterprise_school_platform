package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 课程章节表，存储课程的章节结构和学习资源
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-08
 */
@Getter
@Setter
@TableName("course_chapter")
public class CourseChapter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 章节ID，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程ID
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 章节名称
     */
    @TableField("name")
    private String name;

    /**
     * 章节顺序，用于排序显示
     */
    @TableField("sequence")
    private Integer sequence;

    /**
     * 章节类型：0视频 1文档 2问答 3其他
     */
    @TableField("type")
    private Byte type;

    /**
     * 资源路径
     */
    @TableField("resource_path")
    private String resourcePath;
}
