package com.company.PersonalBlog.controller;

import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.MessageDto;
import com.company.PersonalBlog.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "Create Message Methods")
    @PostMapping
    public HttpApiResponse<MessageDto> createMessage(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("dto") @Valid MessageDto messageDto
    ) {
        return messageService.createMessage(messageDto, file);
    }

    @Operation(summary = "Get example data", description = "Returns an example response")
    @ApiResponse(responseCode = "200", description = "Successful response")
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

    @GetMapping("/{id}/get-all-list")
    public HttpApiResponse<MessageDto> getAllMessages(@PathVariable("id") Integer id) {
        return this.messageService.getMessageAllFields(id);
    }
}
