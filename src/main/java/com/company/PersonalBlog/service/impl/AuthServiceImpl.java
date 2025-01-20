package com.company.PersonalBlog.service.impl;

import com.company.PersonalBlog.dto.AuthUserDto;
import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.exceptions.CustomBadRequestExceptions;
import com.company.PersonalBlog.models.AuthUser;
import com.company.PersonalBlog.repository.AuthUserRepository;
import com.company.PersonalBlog.service.AuthService;
import com.company.PersonalBlog.service.JwtService;
import com.company.PersonalBlog.service.mapper.AuthUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthUserRepository authUserRepository;
    private final AuthUserMapper authUserMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public HttpApiResponse<AuthUserDto> createAuthUser(AuthUserDto dto) {
        try {
            dto.setPassword(encoder.encode(dto.getPassword()));
            return HttpApiResponse.<AuthUserDto>builder()
                    .message("OK")
                    .code(HttpStatus.OK.value())
                    .content(this.authUserMapper.toDto(
                            this.authUserRepository.saveAndFlush(
                                    this.authUserMapper.toEntity(dto)
                            )
                    ))
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<AuthUserDto> getAuthUserById(Integer id) {
        try {
            if (this.authUserRepository.existsById(id)) {
                Optional<AuthUser> authUser = this.authUserRepository.findById(id);
                if (authUser.isPresent()) {
                    return HttpApiResponse.<AuthUserDto>builder()
                            .message("OK")
                            .code(HttpStatus.OK.value())
                            .content(this.authUserMapper.toDto(authUser.get()))
                            .build();
                }
            }
            return HttpApiResponse.<AuthUserDto>builder()
                    .message("Not Found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<AuthUserDto> updateAuthUser(Integer id, AuthUserDto dto) {
        try {
            if (this.authUserRepository.existsById(id)) {
                Optional<AuthUser> optionalAuthUser = this.authUserRepository.findById(id);
                if (optionalAuthUser.isPresent()) {
                    AuthUser updateAuthUser = this.authUserMapper.updateUser(optionalAuthUser.get(), dto);
                    return HttpApiResponse.<AuthUserDto>builder()
                            .message("OK")
                            .code(HttpStatus.OK.value())
                            .content(authUserMapper.toDto(updateAuthUser))
                            .build();
                }
            }
            return HttpApiResponse.<AuthUserDto>builder()
                    .message("Not Found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public HttpApiResponse<String> deleteAuthUserById(Integer id) {
        try {
            if (this.authUserRepository.existsById(id)) {
                this.authUserRepository.deleteById(id);
                return HttpApiResponse.<String>builder()
                        .message("User Deleted Successfully")
                        .code(HttpStatus.OK.value())
                        .build();
            }
            return HttpApiResponse.<String>builder()
                    .message("Not Found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
        } catch (Exception e) {
            throw new CustomBadRequestExceptions(e.getMessage());
        }
    }

    @Override
    public String login(AuthUserDto dto) {
        Authentication authentication = this.authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        if (authentication.isAuthenticated()) {
            System.out.println(this.jwtService.getJwtToken(dto.getUsername()));
            return this.jwtService.getJwtToken(dto.getUsername());

        }
        return "Failed to login";
    }
}
