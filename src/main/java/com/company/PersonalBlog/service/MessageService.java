package com.company.PersonalBlog.service;

import com.company.PersonalBlog.dto.CommentDto;
import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.MessageDto;
import com.company.PersonalBlog.models.Comment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface MessageService {
    HttpApiResponse<MessageDto> createMessage(MessageDto messageDto, MultipartFile file);

    HttpApiResponse<MessageDto> getMessageById(Integer id);

    HttpApiResponse<MessageDto> updateMessage(Integer id, MessageDto messageDto);

    HttpApiResponse<String> deleteMessage(Integer id);

    HttpApiResponse<MessageDto> getMessageAllFields(Integer id);
    List<CommentDto> getCommentsByMessageId(Integer id);
}
