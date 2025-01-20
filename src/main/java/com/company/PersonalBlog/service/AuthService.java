package com.company.PersonalBlog.service;

import com.company.PersonalBlog.dto.AuthUserDto;
import com.company.PersonalBlog.dto.HttpApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    HttpApiResponse<AuthUserDto> createAuthUser(AuthUserDto dto);

    HttpApiResponse<AuthUserDto> getAuthUserById(Integer id);

    HttpApiResponse<AuthUserDto> updateAuthUser(Integer id, AuthUserDto dto);

    HttpApiResponse<String> deleteAuthUserById(Integer id);

    String login(AuthUserDto dto);
}
