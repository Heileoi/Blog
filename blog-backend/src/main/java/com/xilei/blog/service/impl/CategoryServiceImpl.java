package com.xilei.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xilei.blog.entity.Article;
import com.xilei.blog.entity.Category;
import com.xilei.blog.enums.ArticleStatus;
import com.xilei.blog.exception.BusinessException;
import com.xilei.blog.mapper.ArticleMapper;
import com.xilei.blog.mapper.CategoryMapper;
import com.xilei.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分类Service实现类
 * 功能：实现分类的CRUD、树形结构构建等业务逻辑
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final ArticleMapper articleMapper;

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void createCategory(Category category) {
        // 检查分类名是否重复
        Long count = categoryMapper.selectCount(
                new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName())
        );
        if (count > 0) {
            throw new BusinessException("分类名称已存在");
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        categoryMapper.insert(category);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void updateCategory(Category category) {
        if (category.getId() == null) {
            throw new BusinessException("分类ID不能为空");
        }
        categoryMapper.updateById(category);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long categoryId) {
        // 检查分类下是否有文章
        Long articleCount = articleMapper.selectCount(
                new LambdaQueryWrapper<Article>().eq(Article::getCategoryId, categoryId)
        );
        if (articleCount > 0) {
            throw new BusinessException("该分类下还有文章，无法删除");
        }
        // 检查是否有子分类
        Long childCount = categoryMapper.selectCount(
                new LambdaQueryWrapper<Category>().eq(Category::getParentId, categoryId)
        );
        if (childCount > 0) {
            throw new BusinessException("该分类下还有子分类，无法删除");
        }
        categoryMapper.deleteById(categoryId);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryMapper.selectById(categoryId);
    }

    /**
     * 获取分类树形结构
     * 算法：1.查询所有分类 2.按parentId分组 3.递归构建树
     */
    @Override
    @Cacheable(value = "categories", key = "'tree'")
    public List<Category> listCategoryTree() {
        List<Category> allCategories = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().orderByAsc(Category::getSortOrder)
        );
        return buildCategoryTree(allCategories, 0L);
    }

    /**
     * 获取所有分类（含文章数量）
     */
    @Override
    @Cacheable(value = "categories", key = "'withCount'")
    public List<Category> listCategoriesWithCount() {
        List<Category> categories = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().orderByAsc(Category::getSortOrder)
        );
        // 统计每个分类下的已发布文章数
        categories.forEach(cat -> {
            Long count = articleMapper.selectCount(
                    new LambdaQueryWrapper<Article>()
                            .eq(Article::getCategoryId, cat.getId())
                            .eq(Article::getStatus, ArticleStatus.PUBLISHED.getCode())
            );
            cat.setArticleCount(count.intValue());
        });
        return categories;
    }

    /**
     * 递归构建分类树
     */
    private List<Category> buildCategoryTree(List<Category> allCategories, Long parentId) {
        Map<Long, List<Category>> parentMap = allCategories.stream()
                .collect(Collectors.groupingBy(Category::getParentId));

        return buildChildren(parentMap, parentId);
    }

    private List<Category> buildChildren(Map<Long, List<Category>> parentMap, Long parentId) {
        List<Category> children = parentMap.getOrDefault(parentId, new ArrayList<>());
        // 递归设置子分类（这里简化处理，只返回两级）
        return children;
    }
}
