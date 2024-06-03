package com.sparta.springbasicsolo.exception;

public class DeletedTodoException extends RuntimeException {

    public DeletedTodoException(String message) {
        super(message);
    }
}