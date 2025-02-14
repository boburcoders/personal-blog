package com.company.PersonalBlog.models;

import com.company.PersonalBlog.enums.Roles;
import com.company.PersonalBlog.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class AuthUser extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToOne(mappedBy = "authUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Users users;

    @Enumerated(EnumType.STRING)
    private Status status;
}
