package com.school_enterprise_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 课程章节表，存储课程的章节结构和学习资源
 * </p>
 *
 * @author Naiweilanlan
 * @since 2026-01-06
 */
@Getter
@Setter
@TableName("course_chapter")
@ApiModel(value = "CourseChapter对象", description = "课程章节表，存储课程的章节结构和学习资源")
public class CourseChapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("章节ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("课程ID")
    @TableField("course_id")
    private Long courseId;

    @ApiModelProperty("章节名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("章节顺序，用于排序显示")
    @TableField("sequence")
    private Integer sequence;

    @ApiModelProperty("章节类型：0视频 1文档 2问答 3其他")
    @TableField("type")
    private Byte type;

    @ApiModelProperty("资源路径")
    @TableField("resource_path")
    private String resourcePath;
}
