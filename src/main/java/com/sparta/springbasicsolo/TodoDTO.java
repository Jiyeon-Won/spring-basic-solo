package com.sparta.springbasicsolo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@ToString
public class TodoDTO {
    private Long id;
    private String title;
    private String content;
    private String person;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime;

    public TodoDTO() {
    }

    public TodoDTO(String title, String content, String person, String password) {
        this.title = title;
        this.content = content;
        this.person = person;
        this.password = password;
    }
}