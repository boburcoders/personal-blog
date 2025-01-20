package com.company.PersonalBlog.dto;

import com.company.PersonalBlog.models.Comment;
import com.company.PersonalBlog.models.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageDto {
    private Integer messageId;
    private String content;
    private String text;
    @NotNull(message = "UserId must not be null")
    @NotEmpty(message = "UserId must not be empty")
    private Integer userId;
    private List<CommentDto> comments;
}
