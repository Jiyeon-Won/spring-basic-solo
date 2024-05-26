package com.sparta.springbasicsolo.domain.todo.dto;

import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TodoRequestDTO {

    @Schema(description = "todo 제목")
    @Size(max = 200, message = "최대 200자까지 입력할 수 있습니다.")
    @NotBlank(message = "제목을 반드시 입력해 주세요.")
    private String title;

    @Schema(description = "todo 내용")
    private String content;

    @Schema(description = "담당자", example = "user777@gmail.com")
    @Pattern(regexp = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@(?:[a-zA-Z]+\\.)+[a-zA-Z]{2,7}$", message = "이메일 형식이 올바르지 않습니다.")
    private String person;

    @Schema(description = "작성한 todo의 비밀번호")
    @NotBlank(message = "비밀번호를 반드시 입력해 주세요.")
    private String password;

    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .content(content)
                .person(person)
                .password(password)
                .build();
    }
}