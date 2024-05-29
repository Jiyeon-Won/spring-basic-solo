package com.sparta.springbasicsolo.domain.comment.controller;

import com.sparta.springbasicsolo.domain.CommonResponseDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentActionDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentDeleteDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentRequestDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentResponseDTO;
import com.sparta.springbasicsolo.domain.comment.service.CommentService;
import com.sparta.springbasicsolo.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<CommonResponseDTO<CommentResponseDTO>> saveComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid CommentRequestDTO dto
    ) {
        log.info("등록할 댓글  = {}", dto);
        CommentResponseDTO responseDTO = commentService.saveComment(dto, userDetails);
        log.info("작성된 댓글 = {}", responseDTO);
        return ResponseEntity.ok()
                .body(CommonResponseDTO.<CommentResponseDTO>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("댓글 등록 성공")
                        .data(responseDTO)
                        .build());
    }

    @PutMapping("/comment")
    public ResponseEntity<CommonResponseDTO<CommentResponseDTO>> updateComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid CommentActionDTO dto
    ) {
        log.info("수정할 댓글 = {}", dto);
        CommentResponseDTO responseDTO = commentService.updateComment(dto, userDetails);
        log.info("수정된 댓글 = {}", responseDTO);
        return ResponseEntity.ok()
                .body(CommonResponseDTO.<CommentResponseDTO>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("댓글 수정 성공")
                        .data(responseDTO)
                        .build());
    }

    @DeleteMapping("/comment")
    public ResponseEntity<CommonResponseDTO<Object>> deleteComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid CommentDeleteDTO dto
    ) {
        log.info("삭제할 댓글 = {}", dto);
        commentService.deleteComment(dto, userDetails);
        return ResponseEntity.ok()
                .body(CommonResponseDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("댓글 삭제 성공")
                        .build());
    }
}