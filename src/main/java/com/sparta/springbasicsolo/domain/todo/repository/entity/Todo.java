package com.sparta.springbasicsolo.domain.todo.repository.entity;

import com.sparta.springbasicsolo.domain.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String password;

    private LocalDateTime createdDateTime;

    private Boolean isDeleted;

    @Builder
    public Todo(String title, String content, User user, String password) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.password = password;
        this.createdDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.isDeleted = false;
    }
}