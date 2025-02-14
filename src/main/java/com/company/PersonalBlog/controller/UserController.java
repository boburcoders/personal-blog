package com.company.PersonalBlog.controller;

import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.UsersDto;
import com.company.PersonalBlog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

   /* @PostMapping
    public HttpApiResponse<UsersDto> createUser(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestBody @Valid UsersDto dto) {
        return this.userService.createUser(dto, file);
    }*/

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

    @PostMapping("/{id}/file")
    public HttpApiResponse<UsersDto> uploadFileToUser(
            @PathVariable(value = "id") Integer id,
            @RequestPart("file") MultipartFile file) {
        System.out.println(id + "  " + file.getOriginalFilename());
        return this.userService.uploadFileToUser(id, file);
    }

    @GetMapping("/get-all")
    public HttpApiResponse<List<UsersDto>> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("{id}/get-all-list")
    public HttpApiResponse<UsersDto> getEntityLists(@PathVariable("id") Integer id) {
        return this.userService.getEntityLists(id);
    }
}
