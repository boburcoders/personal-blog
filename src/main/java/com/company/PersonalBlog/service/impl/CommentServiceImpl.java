package com.company.PersonalBlog.service.impl;

import com.company.PersonalBlog.dto.CommentDto;
import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.exceptions.CustomBadRequestExceptions;
import com.company.PersonalBlog.models.Comment;
import com.company.PersonalBlog.repository.CommentRepository;
import com.company.PersonalBlog.service.CommentService;
import com.company.PersonalBlog.service.S3Service;
import com.company.PersonalBlog.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final S3Service s3Service;

    @Override
    public HttpApiResponse<CommentDto> createComment(CommentDto dto, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                validateFile(file);
                String generatedName = UUID.randomUUID() + "-" + file.getOriginalFilename();
                String uploadedFile = s3Service.uploadFile(
                        file.getOriginalFilename(),
                        file.getBytes(),
                        file.getContentType(),
                        generatedName);
                dto.setContent(uploadedFile);
            }

            Comment savedComment = commentRepository.saveAndFlush(commentMapper.toEntity(dto));

            return HttpApiResponse.<CommentDto>builder()
                    .code(HttpStatus.CREATED.value())
                    .message("Comment created successfully!")
                    .content(commentMapper.toDto(savedComment))
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions("Failed to create comment");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) { // 10 MB limit
            throw new CustomBadRequestExceptions("File size exceeds the maximum limit");
        }

        if (!file.getContentType().startsWith("image/")) { // Restrict to image files
            throw new CustomBadRequestExceptions("Only image files are allowed");
        }
    }


    @Override
    public HttpApiResponse<CommentDto> getCommentById(Integer id) {
        try {
            if (!this.commentRepository.existsById(id)) {
                throw new CustomBadRequestExceptions("Comment with id " + id + " does not exist");
            }
            Optional<Comment> optionalComment = this.commentRepository.findById(id);
            if (optionalComment.isEmpty()) {
                throw new CustomBadRequestExceptions("Comment with id " + id + " does not exist");
            }
            return HttpApiResponse.<CommentDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("Ok")
                    .content(this.commentMapper.toDto(optionalComment.get()))
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<CommentDto> updateComment(Integer id, CommentDto dto) {
        try {
            if (!this.commentRepository.existsById(id)) {
                throw new CustomBadRequestExceptions("Comment with id " + id + " does not exist");
            }
            Optional<Comment> optionalComment = this.commentRepository.findById(id);
            if (optionalComment.isEmpty()) {
                throw new CustomBadRequestExceptions("Comment with id " + id + " does not exist");
            }
            Comment updatedComment = this.commentMapper.updateAllFields(optionalComment.get(), dto);
            return HttpApiResponse.<CommentDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("Ok")
                    .content(this.commentMapper.toDto(updatedComment))
                    .build();

        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<String> deleteCommentById(Integer id) {
        try {
            if (!this.commentRepository.existsById(id)) {
                throw new CustomBadRequestExceptions("Comment with id " + id + " does not exist");
            }
            this.commentRepository.deleteById(id);
            return HttpApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Comment Deleted Successfully")
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }
}
