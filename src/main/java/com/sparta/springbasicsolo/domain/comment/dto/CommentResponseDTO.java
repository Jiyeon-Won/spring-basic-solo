package com.sparta.springbasicsolo.domain.comment.dto;

import com.sparta.springbasicsolo.domain.comment.repository.entity.Comment;
import lombok.Builder;
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
    private Long commentId;
    private LocalDateTime createdDateTime;

    public CommentResponseDTO(String content, Long userId, Long commentId, LocalDateTime createdDateTime) {
        this.content = content;
        this.userId = userId;
        this.commentId = commentId;
        this.createdDateTime = createdDateTime;
    }

    public CommentResponseDTO(String content, Long commentId, LocalDateTime createdDateTime) {
        this.content = content;
        this.commentId = commentId;
        this.createdDateTime = createdDateTime;
    }
}