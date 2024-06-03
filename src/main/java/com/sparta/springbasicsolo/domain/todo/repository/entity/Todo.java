package com.sparta.springbasicsolo.domain.todo.repository.entity;

import com.sparta.springbasicsolo.domain.file.repository.entity.File;
import com.sparta.springbasicsolo.domain.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    @OneToOne(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private File file;

    private Boolean isDeleted;

    @Builder
    public Todo(String title, String content, User user, String password) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdDateTime = LocalDateTime.now();
        this.updatedDateTime = LocalDateTime.now();
        this.isDeleted = false;
    }
}