package com.xilei.blog.controller.admin;

import com.xilei.blog.common.Result;
import com.xilei.blog.entity.Tag;
import com.xilei.blog.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台标签管理Controller
 * 功能：管理员对标签的增删改查操作
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "后台标签管理", description = "管理员标签CRUD接口")
@RestController
@RequestMapping("/admin/tag")
@RequiredArgsConstructor
public class AdminTagController {

    private final TagService tagService;

    @Operation(summary = "创建标签")
    @PostMapping
    public Result<Void> createTag(@RequestBody Tag tag) {
        tagService.createTag(tag);
        return Result.success("标签创建成功", null);
    }

    @Operation(summary = "更新标签")
    @PutMapping
    public Result<Void> updateTag(@RequestBody Tag tag) {
        tagService.updateTag(tag);
        return Result.success("标签更新成功", null);
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success("标签删除成功", null);
    }

    @Operation(summary = "获取标签列表")
    @GetMapping("/list")
    public Result<List<Tag>> listTags() {
        return Result.success(tagService.listTagsWithCount());
    }
}
