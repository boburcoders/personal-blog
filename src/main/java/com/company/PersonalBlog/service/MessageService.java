package com.company.PersonalBlog.service;

import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.MessageDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MessageService {
    HttpApiResponse<MessageDto> createMessage(MessageDto messageDto, MultipartFile file);

    HttpApiResponse<MessageDto> getMessageById(Integer id);

    HttpApiResponse<MessageDto> updateMessage(Integer id, MessageDto messageDto);

    HttpApiResponse<String> deleteMessage(Integer id);
}
