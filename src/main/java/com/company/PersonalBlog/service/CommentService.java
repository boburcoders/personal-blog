package com.company.PersonalBlog.service;

import com.company.PersonalBlog.dto.CommentDto;
import com.company.PersonalBlog.dto.HttpApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CommentService {
    HttpApiResponse<CommentDto> createComment(CommentDto dto, MultipartFile file);

    HttpApiResponse<CommentDto> getCommentById(Integer id);

    HttpApiResponse<CommentDto> updateComment(Integer id, CommentDto dto);

    HttpApiResponse<String> deleteCommentById(Integer id);
}
