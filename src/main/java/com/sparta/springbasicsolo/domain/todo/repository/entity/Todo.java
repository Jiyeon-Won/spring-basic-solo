package com.sparta.springbasicsolo.domain.todo.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @Setter
    private String title;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;

    @Setter
    private String person;

    private String password;

    private LocalDateTime createdDateTime;

    @Setter
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