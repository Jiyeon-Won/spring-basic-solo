package com.sparta.springbasicsolo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime;

    public TodoDTO() {
    }

    public TodoDTO(Long id, String title, String content, String person, String password, LocalDateTime createdDateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.person = person;
        this.password = password;
        this.createdDateTime = createdDateTime;
    }

    public TodoDTO(Long id, String title, String content, String person, String password) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.person = person;
        this.password = password;
        this.createdDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}