package com.xilei.blog.controller.admin;

import com.xilei.blog.common.PageResult;
import com.xilei.blog.common.Result;
import com.xilei.blog.entity.UploadFile;
import com.xilei.blog.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后台文件管理Controller
 * 功能：文件上传、删除、查询
 */
@Tag(name = "后台文件管理", description = "文件上传管理接口")
@RestController
@RequestMapping("/admin/file")
@RequiredArgsConstructor
public class AdminFileController {

    private final FileService fileService;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = fileService.uploadFile(file);
        return Result.success("上传成功", url);
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/{id}")
    public Result<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return Result.success("文件删除成功", null);
    }

    @Operation(summary = "分页查询文件")
    @GetMapping("/list")
    public Result<PageResult<UploadFile>> listFiles(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String mimeType) {
        return Result.success(fileService.listFiles(pageNum, pageSize, mimeType));
    }
}
