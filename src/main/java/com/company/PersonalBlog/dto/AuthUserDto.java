package com.company.PersonalBlog.dto;

import com.company.PersonalBlog.enums.Roles;
import com.company.PersonalBlog.enums.Status;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto {

    private Integer authId;
    @NotNull(message = "Username must not be null")
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotNull(message = "Password must not be null")
    @NotBlank(message = "Password must not be blank")
    private String password;
    private Integer userId;
    private Roles role;
    private Status status;

}
