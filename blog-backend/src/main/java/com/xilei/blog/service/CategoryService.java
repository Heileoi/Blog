package com.xilei.blog.service;

import com.xilei.blog.entity.Category;

import java.util.List;

/**
 * 分类Service接口
 * 功能：定义分类相关的业务逻辑方法
 */
public interface CategoryService {

    /**
     * 创建分类
     * @param category 分类信息
     */
    void createCategory(Category category);

    /**
     * 更新分类
     * @param category 分类信息
     */
    void updateCategory(Category category);

    /**
     * 删除分类
     * @param categoryId 分类ID
     */
    void deleteCategory(Long categoryId);

    /**
     * 获取分类详情
     * @param categoryId 分类ID
     * @return 分类信息
     */
    Category getCategoryById(Long categoryId);

    /**
     * 获取所有分类列表（树形结构）
     * @return 分类树
     */
    List<Category> listCategoryTree();

    /**
     * 获取所有分类列表（平级，含文章数量）
     * @return 分类列表
     */
    List<Category> listCategoriesWithCount();
}
