package com.company.PersonalBlog.service.impl;

import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.UsersDto;
import com.company.PersonalBlog.exceptions.CustomBadRequestExceptions;
import com.company.PersonalBlog.models.Users;
import com.company.PersonalBlog.service.S3Service;
import com.company.PersonalBlog.service.mapper.UserMapper;
import com.company.PersonalBlog.repository.UserRepository;
import com.company.PersonalBlog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final S3Service s3Service;

    @Override
    public HttpApiResponse<UsersDto> createUser(UsersDto dto, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                validateFile(file);
                String generatedName = UUID.randomUUID() + "-" + file.getOriginalFilename();
                String uploadedFile = s3Service.uploadFile(
                        file.getOriginalFilename(),
                        file.getBytes(),
                        file.getContentType(),
                        generatedName);
                dto.setAvatar(uploadedFile);
            }
            Users savedUser = this.userRepository.saveAndFlush(this.userMapper.toEntity(dto));
            return HttpApiResponse.<UsersDto>builder()
                    .code(HttpStatus.CREATED.value())
                    .message("OK")
                    .content(this.userMapper.toDto(savedUser))
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) { // 10 MB limit
            throw new CustomBadRequestExceptions("File size exceeds the maximum limit");
        }

    }

    @Override
    public HttpApiResponse<UsersDto> getUserById(Integer id) {
        try {
            if (!this.userRepository.existsById(id)) {
                return HttpApiResponse.<UsersDto>builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("Not Found")
                        .build();
            }
            Optional<Users> user = this.userRepository.findById(id);
            if (user.isPresent()) {
                return HttpApiResponse.<UsersDto>builder()
                        .code(HttpStatus.OK.value())
                        .message("OK")
                        .content(this.userMapper.toDto(user.get()))
                        .build();
            }
            return HttpApiResponse.<UsersDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Not Found")
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<UsersDto> updateUserById(Integer id, UsersDto dto) {
        try {
            if (!this.userRepository.existsById(id)) {
                return HttpApiResponse.<UsersDto>builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("Not Found")
                        .build();
            }
            Optional<Users> user = this.userRepository.findById(id);
            if (user.isPresent()) {
                Users updateUser = this.userMapper.updateUser(user.get(), dto);
                return HttpApiResponse.<UsersDto>builder()
                        .code(HttpStatus.OK.value())
                        .message("OK")
                        .content(this.userMapper.toDto(updateUser))
                        .build();
            }
            return HttpApiResponse.<UsersDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Not Found")
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<String> deleteUserById(Integer id) {
        try {
            if (!this.userRepository.existsById(id)) {
                return HttpApiResponse.<String>builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("Not Found")
                        .build();
            }
            Optional<Users> user = this.userRepository.findById(id);
            if (user.isPresent()) {
                this.userRepository.deleteById(id);
            }
            return HttpApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("User deleted successfully")
                    .build();

        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }

    }
}
