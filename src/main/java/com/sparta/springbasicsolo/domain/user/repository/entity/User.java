package com.sparta.springbasicsolo.domain.user.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String username;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;
    private LocalDateTime createdDateTime;

    public User(String nickname, String username, String password, UserRoleEnum role) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}