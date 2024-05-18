package com.sparta.springbasicsolo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "todo")
public class TodoDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "todo 인덱스 번호")
    private Long id;

    @Schema(description = "todo 제목")
    @Size(max = 200, message = "최대 200자까지 입력할 수 있습니다.")
    @NotBlank(message = "제목을 반드시 입력해 주세요.")
    private String title;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "todo 내용")
    private String content;

    @Schema(description = "담당자", example = "user777@gmail.com")
    @Pattern(regexp = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@(?:[a-zA-Z]+\\.)+[a-zA-Z]{2,7}$", message = "이메일 형식이 올바르지 않습니다.")
    private String person;

    @Schema(description = "작성한 todo의 비밀번호")
    @NotBlank(message = "비밀번호를 반드시 입력해 주세요.")
    private String password;

    @Schema(description = "작성한 날짜", example = "2024-05-16 12:36:40")
    private LocalDateTime createdDateTime;

    @Schema(description = "todo 삭제 여부", example = "false")
    private Boolean isDeleted;

    @Builder
    public TodoDTO(Long id, String title, String content, String person, String password) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.person = person;
        this.password = password;
        this.createdDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.isDeleted = false;
    }

    public TodoDTO(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public TodoDTO(String title, String content, String person, String password, boolean isDeleted) {
        this.title = title;
        this.content = content;
        this.person = person;
        this.password = password;
        this.isDeleted = isDeleted;
    }
}