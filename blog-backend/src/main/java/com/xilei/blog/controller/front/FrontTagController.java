package com.xilei.blog.controller.front;

import com.xilei.blog.common.Result;
import com.xilei.blog.entity.Tag;
import com.xilei.blog.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台标签Controller
 * 功能：提供前台标签展示接口
 */
@Tags({@Tag(name = "前台标签", description = "前台标签展示接口")})
@RestController
@RequestMapping("/front/tag")
@RequiredArgsConstructor
public class FrontTagController {

    private final TagService tagService;

    @Operation(summary = "获取所有标签")
    @GetMapping("/list")
    public Result<List<com.xilei.blog.entity.Tag>> listTags() {
        return Result.success(tagService.listTagsWithCount());
    }

    @Operation(summary = "获取热门标签")
    @GetMapping("/hot")
    public Result<List<com.xilei.blog.entity.Tag>> getHotTags(
            @RequestParam(defaultValue = "20") Integer limit) {
        return Result.success(tagService.getHotTags(limit));
    }
}
