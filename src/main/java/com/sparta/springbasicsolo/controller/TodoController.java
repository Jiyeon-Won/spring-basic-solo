package com.sparta.springbasicsolo.controller;

import com.sparta.springbasicsolo.controller.dto.CommonResponseDTO;
import com.sparta.springbasicsolo.controller.dto.TodoRequestDTO;
import com.sparta.springbasicsolo.controller.dto.TodoResponseDTO;
import com.sparta.springbasicsolo.exception.ExceptionDTO;
import com.sparta.springbasicsolo.repository.entity.Todo;
import com.sparta.springbasicsolo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "todo 컨트롤러", description = "todo API")
public class TodoController {

    private final TodoService todoService;

    // 일정 저장
    @PostMapping("/todo")
    @Operation(summary = "todo 저장", description = "todo 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> addTodo(@Valid @RequestBody TodoRequestDTO dto) {
        log.info("입력한 todoRequestDTO: {}", dto);
        Optional<Todo> savedTodo = todoService.addTodo(dto);
        log.info("저장된 todoDTO: {}", savedTodo);
        if (savedTodo.isPresent()) {
            TodoResponseDTO responseDTO = new TodoResponseDTO(savedTodo.get());
            return ResponseEntity.ok()
                    .body(CommonResponseDTO.<TodoResponseDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .data(responseDTO)
                            .build());
        }
        throw new RuntimeException("서버 내부 오류");
    }

    // 단일 일정 조회
    @GetMapping("/todo/{id}")
    @Operation(summary = "todo 단일 조회", description = "id값을 받아 1개의 todo만 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 조회 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> detail(@PathVariable Long id) {
        Optional<Todo> findTodo = todoService.findById(id);
        log.info("단일 조회 todoDTO: {}", findTodo);
        if (findTodo.isPresent()) {
            TodoResponseDTO responseDTO = new TodoResponseDTO(findTodo.get());
            return ResponseEntity.ok()
                    .body(CommonResponseDTO.<TodoResponseDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .data(responseDTO)
                            .build());
        }
        throw new RuntimeException("서버 내부 오류");
    }

    // 전체 일정 조회
    @GetMapping("/todo")
    @Operation(summary = "todo 전체 조회", description = "전체 todo 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 조회 성공"
                    , content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TodoResponseDTO.class), minItems = 2)))
    })
    public ResponseEntity<CommonResponseDTO<List<TodoResponseDTO>>> findAll() {
        List<Todo> todoList = todoService.findAll();
        log.info("전체 조회 todoList: {}", todoList.stream().map(Todo::toString)
                .collect(Collectors.joining(", ", "[", "]")));

        if (!todoList.isEmpty()) {
            List<TodoResponseDTO> responseList = todoList.stream().map(TodoResponseDTO::new).toList();
            return ResponseEntity.ok()
                    .body(CommonResponseDTO.<List<TodoResponseDTO>>builder()
                            .statusCode(HttpStatus.OK.value())
                            .data(responseList)
                            .build());
        }
        throw new RuntimeException("서버 내부 오류");
    }

    // 일정 수정
    @PutMapping("/todo/{id}")
    @Operation(summary = "todo 수정", description = "id값을 받아 1개의 todo 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 수정 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "비밀 번호가 일치하지 않습니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequestDTO dto) {
        log.info("입력한 todoDTO: {}", dto);
        Optional<Todo> updateTodo = todoService.updateTodo(id, dto);
        log.info("수정한 todoDTO: {}", updateTodo);
        if (updateTodo.isPresent()) {
            TodoResponseDTO responseDTO = new TodoResponseDTO(updateTodo.get());
            return ResponseEntity.ok()
                    .body(CommonResponseDTO.<TodoResponseDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .data(responseDTO)
                            .build());
        }
        throw new RuntimeException("서버 내부 오류");
    }

    // 일정 삭제
    @DeleteMapping(value = "/todo/{id}")
    @Operation(summary = "todo 삭제", description = "id값을 받아 1개의 todo 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 삭제 성공"
                    , content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "\"200 OK\""))),
            @ApiResponse(responseCode = "401", description = "비밀 번호가 일치하지 않습니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<Void>> deleteTodo(@PathVariable Long id, @RequestBody Map<String, String> map) {
        String password = map.get("password");
        log.info("입력한 id:{}, password:{}", id, password);
        todoService.deleteTodo(id, password);
        return ResponseEntity.ok()
                .body(CommonResponseDTO.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
}