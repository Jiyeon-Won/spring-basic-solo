package com.sparta.springbasicsolo.domain.comment.repository.entity;

import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import com.sparta.springbasicsolo.domain.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    private LocalDateTime createdDateTime;

    // 필요할 것 같으면 사용
//    @OneToMany(mappedBy = "todo")
//    private List<Comment> comments;

    @Builder
    public Comment(String content, User user, Todo todo) {
        this.content = content;
        this.user = user;
        this.todo = todo;
        this.createdDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}