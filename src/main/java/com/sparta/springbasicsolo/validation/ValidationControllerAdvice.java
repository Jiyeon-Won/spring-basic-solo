package com.sparta.springbasicsolo.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> responseValid = new LinkedHashMap<>();
        responseValid.put("statusCode", HttpStatus.BAD_REQUEST.toString());
        ex.getBindingResult().getAllErrors()
                .forEach(v -> responseValid.put(((FieldError) v).getField(), v.getDefaultMessage()));
        return ResponseEntity.badRequest().body(responseValid);
    }
}