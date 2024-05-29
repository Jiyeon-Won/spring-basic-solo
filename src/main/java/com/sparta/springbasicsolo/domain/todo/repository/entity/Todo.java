package com.sparta.springbasicsolo.domain.todo.repository.entity;

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

    private String person;

    private String password;

    private LocalDateTime createdDateTime;

    private Boolean isDeleted;

    @Builder
    public Todo(String title, String content, String person, String password) {
        this.title = title;
        this.content = content;
        this.person = person;
        this.password = password;
        this.createdDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.isDeleted = false;
    }
}