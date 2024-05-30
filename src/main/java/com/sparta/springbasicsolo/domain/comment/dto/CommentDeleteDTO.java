package com.sparta.springbasicsolo.domain.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDeleteDTO {

    @NotNull(message = "일정의 ID를 입력해주세요.")
    private Long todoId;

    @NotNull(message = "유저 ID를 입력해주세요.")
    private Long userId;

    @NotNull(message = "댓글 ID를 입력해주세요.")
    private Long commentId;
}