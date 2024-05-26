package com.sparta.springbasicsolo.domain.comment.controller;

import com.sparta.springbasicsolo.domain.CommonResponseDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentRequestDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentResponseDTO;
import com.sparta.springbasicsolo.domain.comment.service.CommentService;
import com.sparta.springbasicsolo.domain.todo.dto.TodoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<CommonResponseDTO<CommentResponseDTO>> saveComment(@RequestBody @Valid CommentRequestDTO dto) {
        log.info("댓글 request dto = {}", dto);
        CommentResponseDTO responseDTO = commentService.saveComment(dto);
        log.info("작성된 댓글 = {}", responseDTO);
        return ResponseEntity.ok()
                .body(CommonResponseDTO.<CommentResponseDTO>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("댓글 등록 성공")
                        .data(responseDTO)
                        .build());
    }
}