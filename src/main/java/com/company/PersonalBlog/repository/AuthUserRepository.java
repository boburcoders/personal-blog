package com.company.PersonalBlog.repository;

import com.company.PersonalBlog.models.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    AuthUser findByUsername(String username);
}
