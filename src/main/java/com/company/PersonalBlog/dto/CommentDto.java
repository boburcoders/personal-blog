package com.company.PersonalBlog.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {
    private Integer commentId;
    private String content;
    private String body;
    @NotNull(message = "UserId must not be null")
    private Integer userId;
    @NotNull(message = "MessageID must not be null")
    private Integer messageId;
}
