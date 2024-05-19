package com.sparta.springbasicsolo.exception;

import com.sparta.springbasicsolo.controller.CommonResponseDTO;
import com.sparta.springbasicsolo.controller.filedto.FileResponseDTO;
import com.sparta.springbasicsolo.controller.tododto.TodoResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> passwordNotMatchedException(PasswordNotMatchedException e) {
        log.error("비밀번호 일치 에러", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponseDTO.<TodoResponseDTO>builder()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(DeletedTodoException.class)
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> deletedTodoException(DeletedTodoException e) {
        log.error("일정이 삭제되었음", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponseDTO.<TodoResponseDTO>builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> emptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error("DB 조회가 안됨", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponseDTO.<TodoResponseDTO>builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> runtimeException(RuntimeException e) {
        log.error("서버 내부 오류", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponseDTO.<TodoResponseDTO>builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<CommonResponseDTO<FileResponseDTO>> fileException(FileException e) {
        log.error("파일 저장 실패", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponseDTO.<FileResponseDTO>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build());
    }
}