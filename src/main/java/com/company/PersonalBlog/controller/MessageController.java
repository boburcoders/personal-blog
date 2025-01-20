package com.company.PersonalBlog.controller;

import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.MessageDto;
import com.company.PersonalBlog.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public HttpApiResponse<MessageDto> createMessage(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestBody @Valid MessageDto messageDto) {
        return this.messageService.createMessage(messageDto, file);
    }

    @GetMapping("/{id}")
    public HttpApiResponse<MessageDto> getMessageById(@PathVariable("id") Integer id) {
        return this.messageService.getMessageById(id);
    }

    @PutMapping("/{id}")
    public HttpApiResponse<MessageDto> updateMessage(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable("id") Integer id,
            @RequestBody @Valid MessageDto messageDto) {

        return this.messageService.updateMessage(id, messageDto);
    }

    @DeleteMapping("/{id}")
    public HttpApiResponse<String> deleteMessage(@PathVariable("id") Integer id) {
        return this.messageService.deleteMessage(id);
    }
}
