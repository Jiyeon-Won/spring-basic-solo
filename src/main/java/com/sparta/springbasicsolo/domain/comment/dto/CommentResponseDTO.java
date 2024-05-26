package com.sparta.springbasicsolo.domain.comment.dto;

import com.sparta.springbasicsolo.domain.comment.repository.entity.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponseDTO {

    private String content;
    private Long userId;
    private LocalDateTime createdDateTime;

    public CommentResponseDTO(Comment comment) {
        this.content = comment.getContent();
        this.userId = comment.getUserId();
        this.createdDateTime = comment.getCreatedDateTime();
    }
}