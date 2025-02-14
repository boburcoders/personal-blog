package com.company.PersonalBlog.dto;

import com.company.PersonalBlog.models.Comment;
import com.company.PersonalBlog.models.Message;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsersDto {
    private Integer userId;
    @NotNull(message = "Email should not be null")
    @Email(message = "Invalid Email format")
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private List<MessageDto> message;
    private List<CommentDto> comments;
    private Integer authUserId;
}
