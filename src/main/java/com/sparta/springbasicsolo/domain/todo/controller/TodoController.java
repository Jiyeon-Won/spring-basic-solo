package com.sparta.springbasicsolo.domain.todo.controller;

import com.sparta.springbasicsolo.domain.CommonResponseDTO;
import com.sparta.springbasicsolo.domain.todo.dto.TodoRequestDTO;
import com.sparta.springbasicsolo.domain.todo.dto.TodoResponseDTO;
import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import com.sparta.springbasicsolo.domain.todo.service.TodoService;
import com.sparta.springbasicsolo.security.service.UserDetailsImpl;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
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
            @ApiResponse(responseCode = "200", description = "일정 저장 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> addTodo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestPart TodoRequestDTO dto,
            @RequestPart MultipartFile file
    ) {
        log.info("입력한 todoRequestDTO: {}, file: {}", dto, file.getOriginalFilename());
        Todo savedTodo = todoService.addTodo(dto, file, userDetails);
        log.info("저장된 todoDTO: {}", savedTodo);

        TodoResponseDTO responseDTO = new TodoResponseDTO(savedTodo);
        return ResponseEntity.ok()
                .body(CommonResponseDTO.<TodoResponseDTO>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("일정 등록 성공")
                        .data(responseDTO)
                        .build());
    }

    // 단일 일정 조회
    @GetMapping("/todo/{todoId}")
    @Operation(summary = "todo 단일 조회", description = "id값을 받아 1개의 todo만 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 조회 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> detail(@PathVariable Long todoId) {
        Todo findTodo = todoService.findById(todoId);
        log.info("단일 조회 todoDTO: {}", findTodo);

        TodoResponseDTO responseDTO = new TodoResponseDTO(findTodo);
        return ResponseEntity.ok()
                .body(CommonResponseDTO.<TodoResponseDTO>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("일정 조회 성공")
                        .data(responseDTO)
                        .build());
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

        List<TodoResponseDTO> responseList = todoList.stream().map(TodoResponseDTO::new).toList();
        return ResponseEntity.ok()
                .body(CommonResponseDTO.<List<TodoResponseDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("일정 조회 성공")
                        .data(responseList)
                        .build());
    }

    // 일정 수정
    @PutMapping("/todo/{todoId}")
    @Operation(summary = "todo 수정", description = "id값을 받아 1개의 todo 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 수정 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "비밀 번호가 일치하지 않습니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<TodoResponseDTO>> updateTodo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long todoId,
            @RequestPart @Valid TodoRequestDTO dto,
            @RequestPart MultipartFile file
    ) {
        log.info("입력한 todoDTO: {}, file: {}", dto, file.getOriginalFilename());
        Todo updateTodo = todoService.updateTodo(todoId, dto, file, userDetails);
        log.info("수정한 todoDTO: {}", updateTodo);

        TodoResponseDTO responseDTO = new TodoResponseDTO(updateTodo);
        return ResponseEntity.ok()
                .body(CommonResponseDTO.<TodoResponseDTO>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("일정 수정 성공")
                        .data(responseDTO)
                        .build());
    }

    // 일정 삭제
    @DeleteMapping(value = "/todo/{todoId}")
    @Operation(summary = "todo 삭제", description = "id값을 받아 1개의 todo 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 삭제 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "비밀 번호가 일치하지 않습니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<Void>> deleteTodo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long todoId,
            @RequestBody Map<String, String> map
    ) {
        Long userId = Long.valueOf(map.get("userId"));
        log.info("입력한 todoId:{}, userId:{}", todoId, userId);
        todoService.deleteTodo(todoId, userId, userDetails);

        return ResponseEntity.ok()
                .body(CommonResponseDTO.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("일정 삭제 성공")
                        .build());
    }
}