package com.sparta.springbasicsolo.domain.todo.dto;

import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TodoResponseDTO {

    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdDateTime;

    public TodoResponseDTO(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.username = todo.getUser().getUsername();
        this.createdDateTime = todo.getCreatedDateTime();
    }
}