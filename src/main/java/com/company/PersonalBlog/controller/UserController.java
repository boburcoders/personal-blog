package com.company.PersonalBlog.controller;

import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.UsersDto;
import com.company.PersonalBlog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public HttpApiResponse<UsersDto> createUser(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestBody @Valid UsersDto dto) {
        return this.userService.createUser(dto, file);
    }

    @GetMapping("/{id}") // Use a path variable for simplicity
    public HttpApiResponse<UsersDto> getUserById(@PathVariable("id") Integer id) {
        return this.userService.getUserById(id);
    }

    @PutMapping("/{id}") // Use a path variable for the resource being updated
    public HttpApiResponse<UsersDto> updateUserById(
            @PathVariable("id") Integer id,
            @RequestBody @Valid UsersDto dto) {
        return this.userService.updateUserById(id, dto);
    }

    @DeleteMapping("/{id}") // Use a path variable for consistency
    public HttpApiResponse<String> deleteUserById(@PathVariable("id") Integer id) {
        return this.userService.deleteUserById(id);
    }
}
