package com.xilei.blog.controller.admin;

import com.xilei.blog.common.OperationLogAnnotation;
import com.xilei.blog.common.Result;
import com.xilei.blog.entity.Category;
import com.xilei.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台分类管理Controller
 * 功能：管理员对分类的增删改查操作
 */
@Tag(name = "后台分类管理", description = "管理员分类CRUD接口")
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "创建分类")
    @OperationLogAnnotation(module = "分类管理", operation = "创建分类")
    @PostMapping
    public Result<Void> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return Result.success("分类创建成功", null);
    }

    @Operation(summary = "更新分类")
    @OperationLogAnnotation(module = "分类管理", operation = "更新分类")
    @PutMapping
    public Result<Void> updateCategory(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return Result.success("分类更新成功", null);
    }

    @Operation(summary = "删除分类")
    @OperationLogAnnotation(module = "分类管理", operation = "删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success("分类删除成功", null);
    }

    @Operation(summary = "获取分类详情")
    @GetMapping("/{id}")
    public Result<Category> getCategory(@PathVariable Long id) {
        return Result.success(categoryService.getCategoryById(id));
    }

    @Operation(summary = "获取分类列表")
    @GetMapping("/list")
    public Result<List<Category>> listCategories() {
        return Result.success(categoryService.listCategoriesWithCount());
    }
}
