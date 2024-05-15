package com.sparta.springbasicsolo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TodoDTO {
    private Long id;
    private String title;
    private String content;
    private String person;
    private String password;
    private Boolean isDeleted;
    private LocalDateTime createdDateTime;

    public TodoDTO() {
    }

    public TodoDTO(String title, String content, String person, String password) {
        this.title = title;
        this.content = content;
        this.person = person;
        this.password = password;
    }

    public TodoDTO(String title, String content, String person, String password, boolean isDeleted) {
        this.title = title;
        this.content = content;
        this.person = person;
        this.password = password;
        this.isDeleted = isDeleted;
    }
}