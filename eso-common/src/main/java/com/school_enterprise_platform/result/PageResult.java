package com.school_enterprise_platform.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 * @param <T> 当前页数据项类型（如 Course、User 等）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total;         // 总记录数

    private List<T> records;    // 当前页数据集合
}