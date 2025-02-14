package com.company.PersonalBlog.service;

import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.dto.UsersDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {

//    HttpApiResponse<UsersDto> createUser(UsersDto dto, MultipartFile file);

    HttpApiResponse<UsersDto> getUserById(Integer id);

    HttpApiResponse<UsersDto> updateUserById(Integer id, UsersDto dto);

    HttpApiResponse<String> deleteUserById(Integer id);

    HttpApiResponse<UsersDto> uploadFileToUser(Integer id, MultipartFile file);

    HttpApiResponse<List<UsersDto>> getAllUsers();

    HttpApiResponse<UsersDto> getEntityLists(Integer id);
}
