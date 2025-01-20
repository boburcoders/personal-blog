package com.company.PersonalBlog.controller;

import com.company.PersonalBlog.dto.CommentDto;
import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public HttpApiResponse<CommentDto> createComment(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestBody @Valid CommentDto dto) {
        return this.commentService.createComment(dto, file);
    }

    @GetMapping("/{id}")
    public HttpApiResponse<CommentDto> getCommentById(@PathVariable("id") Integer id) {
        return this.commentService.getCommentById(id);
    }

    @PutMapping("/{id}")
    public HttpApiResponse<CommentDto> updateComment(
            @PathVariable("id") Integer id,
            @RequestBody @Valid CommentDto dto) {
        return this.commentService.updateComment(id, dto);
    }

    @DeleteMapping("/{id}")
    public HttpApiResponse<String> deleteCommentById(@PathVariable("id") Integer id) {
        return this.commentService.deleteCommentById(id);
    }
}
