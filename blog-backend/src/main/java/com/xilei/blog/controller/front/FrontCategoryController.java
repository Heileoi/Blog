package com.xilei.blog.controller.front;

import com.xilei.blog.common.Result;
import com.xilei.blog.entity.Category;
import com.xilei.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台分类Controller
 * 功能：提供前台分类展示接口
 */
@Tag(name = "前台分类", description = "前台分类展示接口")
@RestController
@RequestMapping("/front/category")
@RequiredArgsConstructor
public class FrontCategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "获取分类列表（含文章数量）")
    @GetMapping("/list")
    public Result<List<Category>> listCategories() {
        return Result.success(categoryService.listCategoriesWithCount());
    }

    @Operation(summary = "获取分类树")
    @GetMapping("/tree")
    public Result<List<Category>> getCategoryTree() {
        return Result.success(categoryService.listCategoryTree());
    }
}
