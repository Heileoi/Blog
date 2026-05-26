package com.xilei.blog.common;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 * 功能：封装分页查询的结果，包含数据列表和分页信息
 */
@Data
public class PageResult<T> implements Serializable {

    /** 当前页码 */
    private Integer pageNum;

    /** 每页大小 */
    private Integer pageSize;

    /** 总记录数 */
    private Long total;

    /** 总页数 */
    private Integer totalPages;

    /** 数据列表 */
    private List<T> list;

    /** 是否有下一页 */
    private Boolean hasNext;

    /** 是否有上一页 */
    private Boolean hasPrevious;

    public PageResult() {
    }

    public PageResult(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
        this.hasNext = pageNum < totalPages;
        this.hasPrevious = pageNum > 1;
    }

    /**
     * 静态工厂方法：创建分页结果
     */
    public static <T> PageResult<T> of(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        return new PageResult<>(pageNum, pageSize, total, list);
    }
}
