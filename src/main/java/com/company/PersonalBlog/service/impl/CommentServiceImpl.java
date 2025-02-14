package com.company.PersonalBlog.service.impl;

import com.company.PersonalBlog.dto.CommentDto;
import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.exceptions.CustomBadRequestExceptions;
import com.company.PersonalBlog.models.Comment;
import com.company.PersonalBlog.repository.CommentRepository;
import com.company.PersonalBlog.repository.MessageRepository;
import com.company.PersonalBlog.repository.UserRepository;
import com.company.PersonalBlog.service.CommentService;
import com.company.PersonalBlog.service.S3Service;
import com.company.PersonalBlog.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final CommentMapper commentMapper;
    private final S3Service s3Service;

    @Override
    public HttpApiResponse<CommentDto> createComment(CommentDto dto, MultipartFile file) {
        try {
            if (this.userRepository.existsById(dto.getUserId()) && this.messageRepository.existsById(dto.getMessageId())) {
                if (file != null && !file.isEmpty()) {
                    validateFile(file);
                    dto.setContent(handleFileUpload(file));
                }
                Comment savedComment = (commentMapper.toEntity(dto));
                if (this.userRepository.findById(dto.getUserId()).isPresent() && this.messageRepository.findById(dto.getMessageId()).isPresent())
                {
                    savedComment.setUser(this.userRepository.findById(dto.getUserId()).get());
                    savedComment.setMessage(this.messageRepository.findById(dto.getMessageId()).get());
                    commentRepository.saveAndFlush(savedComment);
                }

                return HttpApiResponse.<CommentDto>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Comment created successfully!")
                        .content(commentMapper.toDto(savedComment))
                        .build();
            }
            return HttpApiResponse.<CommentDto>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("User does not exist!")
                    .build();

        } catch (Exception e) {
            throw new CustomBadRequestExceptions("Failed to create comment");
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

    private String handleFileUpload(MultipartFile file) throws IOException {
        validateFile(file);
        String generatedName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        return s3Service.uploadFile(
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType(),
                generatedName);
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) { // 10 MB limit
            throw new CustomBadRequestExceptions("File size exceeds the maximum limit");
        }

        if (!file.getContentType().startsWith("image/")) { // Restrict to image files
            throw new CustomBadRequestExceptions("Only image files are allowed");
        }
    }
}
