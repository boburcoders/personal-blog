package com.company.PersonalBlog.controller;

import com.company.PersonalBlog.dto.AuthUserDto;
import com.company.PersonalBlog.dto.HttpApiResponse;
import com.company.PersonalBlog.repository.AuthUserRepository;
import com.company.PersonalBlog.service.AuthService;
import com.company.PersonalBlog.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public HttpApiResponse<AuthUserDto> createAuthUser(@RequestBody @Valid AuthUserDto dto) {
        return this.authService.createAuthUser(dto);
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthUserDto dto) {
        return this.authService.login(dto);
    }

    @GetMapping("/{id}")
    public HttpApiResponse<AuthUserDto> getAuthUserById(@PathVariable Integer id) {
        return this.authService.getAuthUserById(id);
    }

    @PutMapping("/{id}")
    public HttpApiResponse<AuthUserDto> updateAuthUser(
            @PathVariable Integer id,
            @RequestBody @Valid AuthUserDto dto) {
        return this.authService.updateAuthUser(id, dto);

    }

    @DeleteMapping("/{id}")
    public HttpApiResponse<String> deleteAuthUserById(@PathVariable Integer id) {
        return this.authService.deleteAuthUserById(id);
    }

}
