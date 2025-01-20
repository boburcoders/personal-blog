package com.company.PersonalBlog.service;

import com.company.PersonalBlog.models.AuthUser;
import com.company.PersonalBlog.models.UserPrincipal;
import com.company.PersonalBlog.repository.AuthUserRepository;
import com.company.PersonalBlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = this.authUserRepository.findByUsername(username);
        if (authUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(authUser);
    }
}
