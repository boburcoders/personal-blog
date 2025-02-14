package com.company.PersonalBlog.service.impl;

import com.company.PersonalBlog.dto.CommentDto;
import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.MessageDto;
import com.company.PersonalBlog.dto.UsersDto;
import com.company.PersonalBlog.exceptions.CustomBadRequestExceptions;
import com.company.PersonalBlog.models.AuthUser;
import com.company.PersonalBlog.models.Comment;
import com.company.PersonalBlog.models.Users;
import com.company.PersonalBlog.repository.AuthUserRepository;
import com.company.PersonalBlog.service.S3Service;
import com.company.PersonalBlog.service.mapper.CommentMapper;
import com.company.PersonalBlog.service.mapper.MessageMapper;
import com.company.PersonalBlog.service.mapper.UserMapper;
import com.company.PersonalBlog.repository.UserRepository;
import com.company.PersonalBlog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthUserRepository authUserRepository;
    private final CommentMapper commentMapper;
    private final MessageMapper messageMapper;
    private final S3Service s3Service;

    /*@Override
    public HttpApiResponse<UsersDto> createUser(UsersDto dto, MultipartFile file) {
        try {
            validateAuthUserExists(dto.getAuthUserId());
            AuthUser authUser = authUserRepository.findById(dto.getAuthUserId())
                    .orElseThrow(() -> new CustomBadRequestExceptions("Auth user does not exist"));

            if (file != null && !file.isEmpty()) {
                handleFileUpload(dto, file);
            }

            Users users = saveUser(dto, authUser);
            return buildSuccessResponse(users);
        } catch (CustomBadRequestExceptions | IOException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }*/

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
                        .content(this.userMapper.toDto(this.userRepository.save(updateUser)))
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

    @Override
    public HttpApiResponse<UsersDto> uploadFileToUser(Integer id, MultipartFile file) {
        try {
            if (this.userRepository.existsById(id)) {
                Optional<Users> optionalUsers = this.userRepository.findById(id);
                if (optionalUsers.isPresent()) {
                    if (!file.isEmpty()) {
                        validateFile(file);
                        optionalUsers.get().setAvatar(handleFileUpload(file));

                        return HttpApiResponse.<UsersDto>builder()
                                .code(HttpStatus.OK.value())
                                .message("OK")
                                .content(this.userMapper.toDto(this.userRepository.save(optionalUsers.get())))
                                .build();
                    }
                }
            }
            return HttpApiResponse.<UsersDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Not found")
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<List<UsersDto>> getAllUsers() {
        return HttpApiResponse.<List<UsersDto>>builder()
                .code(HttpStatus.OK.value())
                .message("OK")
                .content(this.userMapper.toDtoUserList(this.userRepository.findAll()))
                .build();
    }

    @Override
    public HttpApiResponse<UsersDto> getEntityLists(Integer id) {
        Optional<Users> optionalUsers = this.userRepository.findById(id);
        if (optionalUsers.isPresent()) {
            Users user = optionalUsers.get();
            return HttpApiResponse.<UsersDto>builder()
                    .message("Ok")
                    .code(HttpStatus.OK.value())
                    .content(this.userMapper.toDtoWithAllEntityUserList(user))
                    .build();
        }

        return HttpApiResponse.<UsersDto>builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Not found")
                .build();
    }

    public List<CommentDto> getCommentsByUserId(Integer id) {

        return this.commentMapper.toDtoCommentList(this.userRepository.findCommentsByUserId(id));
    }

    public List<MessageDto> getMessagesByUserId(Integer id) {
        return this.messageMapper.toDtoMessageList(this.userRepository.findMessagesByUserId(id));
    }


    private void validateAuthUserExists(Integer authUserId) {
        if (!authUserRepository.existsById(authUserId)) {
            throw new CustomBadRequestExceptions("Auth user does not exist");
        }
    }


    private String handleFileUpload(MultipartFile file) throws IOException {
        validateFile(file);
        String generatedName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        return s3Service.uploadFile(
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType(),
                generatedName);
    }


    private void validateFile(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) { // 10 MB limit
            throw new CustomBadRequestExceptions("File size exceeds the maximum limit");
        }

    }

}
