package com.sparta.springbasicsolo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<ExceptionDTO> passwordNotMatchedException(PasswordNotMatchedException e) {
        log.error("비밀번호 일치 에러", e);
        ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionDTO);
    }
}