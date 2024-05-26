package com.sparta.springbasicsolo.domain.comment.repository.entity;

import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String content;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    private LocalDateTime createdDateTime;

    // 필요할 것 같으면 사용
//    @OneToMany(mappedBy = "todo")
//    private List<Comment> comments;

    public Comment(Long id, String content, Long userId, Todo todo) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.todo = todo;
        this.createdDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}