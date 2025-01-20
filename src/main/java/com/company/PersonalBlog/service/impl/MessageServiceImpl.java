package com.company.PersonalBlog.service.impl;

import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.MessageDto;
import com.company.PersonalBlog.exceptions.CustomBadRequestExceptions;
import com.company.PersonalBlog.models.Message;
import com.company.PersonalBlog.repository.MessageRepository;
import com.company.PersonalBlog.service.MessageService;
import com.company.PersonalBlog.service.S3Service;
import com.company.PersonalBlog.service.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final S3Service s3Service;

    @Override
    public HttpApiResponse<MessageDto> createMessage(MessageDto messageDto, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String generatedName = UUID.randomUUID() + "-" + file.getOriginalFilename();
                String uploadedFile = s3Service.uploadFile(
                        file.getOriginalFilename(),
                        file.getBytes(),
                        file.getContentType(),
                        generatedName);
                messageDto.setContent(uploadedFile);
            }
            Message savedMessage = this.messageRepository.saveAndFlush(this.messageMapper.ToEntity(messageDto));

            return HttpApiResponse.<MessageDto>builder()
                    .message("Ok")
                    .code(HttpStatus.CREATED.value())
                    .content(this.messageMapper.ToDto(savedMessage))
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<MessageDto> getMessageById(Integer id) {
        try {
            if (!this.messageRepository.existsById(id)) {
                return HttpApiResponse.<MessageDto>builder()
                        .message("Not Found")
                        .code(HttpStatus.NOT_FOUND.value())
                        .build();
            }
            Optional<Message> optionalMessage = this.messageRepository.findById(id);
            if (!optionalMessage.isPresent()) {
                return HttpApiResponse.<MessageDto>builder()
                        .message("Not Found")
                        .code(HttpStatus.NOT_FOUND.value())
                        .build();
            }
            return HttpApiResponse.<MessageDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("Ok")
                    .content(this.messageMapper.ToDto(optionalMessage.get()))
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<MessageDto> updateMessage(Integer id, MessageDto messageDto) {
        try {
            if (!this.messageRepository.existsById(id)) {
                return HttpApiResponse.<MessageDto>builder()
                        .message("Not Found")
                        .code(HttpStatus.NOT_FOUND.value())
                        .build();
            }
            Optional<Message> optionalMessage = this.messageRepository.findById(id);
            if (optionalMessage.isEmpty()) {
                throw new CustomBadRequestExceptions("Not Found");
            }
            Message updatedMessage = this.messageMapper.updateAllFields(optionalMessage.get(), messageDto);
            return HttpApiResponse.<MessageDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("Ok")
                    .content(this.messageMapper.ToDto(updatedMessage))
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<String> deleteMessage(Integer id) {
        try {
            if (!this.messageRepository.existsById(id)) {
                throw new CustomBadRequestExceptions("Not Found");
            }
            this.messageRepository.deleteById(id);
            return HttpApiResponse.<String>builder()
                    .message("Message Deleted")
                    .code(HttpStatus.OK.value())
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }
}
