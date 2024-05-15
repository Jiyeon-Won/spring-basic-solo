package com.sparta.springbasicsolo.exception;

public class PasswordNotMatchedException extends RuntimeException {

    public PasswordNotMatchedException(String message) {
        super(message);
    }
}