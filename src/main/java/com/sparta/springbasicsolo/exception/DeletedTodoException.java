package com.sparta.springbasicsolo.exception;

public class DeletedTodoException extends RuntimeException {

    public DeletedTodoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedTodoException(String message) {
        super(message);
    }
}