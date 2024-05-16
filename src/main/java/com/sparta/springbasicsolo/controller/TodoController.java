package com.sparta.springbasicsolo.controller;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.TodoForm;
import com.sparta.springbasicsolo.exception.ExceptionDTO;
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

    // 메인 화면
//    @GetMapping("/todo")
//    public String findAll(Model model) {
//        List<TodoDTO> todoList = todoService.findAll();
//        model.addAttribute("items", todoList);
//        return "index";
//    }

    // 일정 상세 페이지
//    @GetMapping("/todo/{id}")
//    public String detail(@PathVariable Long id, Model model) {
//        Optional<TodoDTO> todoDTO = todoService.findById(id);
//        todoDTO.ifPresent(dto -> model.addAttribute("item", dto));
//        return "detail";
//    }

    // 일정 작성 페이지
//    @GetMapping("/todo/write")
//    public String write() {
//        return "write";
//    }

    // 일정 수정 페이지
//    @GetMapping("/todo/write/{id}")
//    public String write(@PathVariable Long id, Model model) {
//        Optional<TodoDTO> todoDTO = todoService.findById(id);
//        todoDTO.ifPresent(dto -> model.addAttribute("item", dto));
//        return "write";
//    }

    // 일정 저장
    @PostMapping("/todo")
    @Operation(summary = "todo 저장", description = "todo 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<TodoDTO> addTodo(@Valid @RequestBody TodoForm todoForm) {
        log.info("입력한 todoForm: {}", todoForm);
        TodoDTO todoDTO = TodoDTO.builder()
                .title(todoForm.getTitle())
                .content(todoForm.getContent())
                .person(todoForm.getPerson())
                .password(todoForm.getPassword())
                .build();
        Optional<TodoDTO> savedTodo = todoService.addTodo(todoDTO);
        log.info("저장된 todoDTO: {}", todoDTO);

        if (savedTodo.isPresent()) {
            return ResponseEntity.ok(savedTodo.get());
        }
        throw new RuntimeException("서버 내부 오류");
    }

    // 단일 일정 조회
    @GetMapping("/todo/{id}")
    @Operation(summary = "todo 단일 조회", description = "id값을 받아 1개의 todo만 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 조회 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoDTO.class))),
            @ApiResponse(responseCode = "404", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<TodoDTO> detail(@PathVariable Long id) {
        Optional<TodoDTO> todoDTO = todoService.findById(id);
        log.info("단일 조회 todoDTO: {}", todoDTO);
        if (todoDTO.isPresent()) {
            return ResponseEntity.ok(todoDTO.get());
        }
        throw new RuntimeException("서버 내부 오류");
    }

    // 전체 일정 조회
    @GetMapping("/todo")
    @Operation(summary = "todo 전체 조회", description = "전체 todo 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 조회 성공"
                    , content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TodoDTO.class), minItems = 2)))
    })
    public ResponseEntity<List<TodoDTO>> findAll() {
        List<TodoDTO> todoList = todoService.findAll();
        log.info("전체 조회 todoList: {}", todoList.stream().map(TodoDTO::toString)
                .collect(Collectors.joining(", ", "[", "]")));

        if (!todoList.isEmpty()) {
            return ResponseEntity.ok(todoList);
        }
        throw new RuntimeException("서버 내부 오류");
    }

    // 일정 수정
    @PutMapping("/todo/{id}")
    @Operation(summary = "todo 수정", description = "id값을 받아 1개의 todo 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 수정 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoDTO.class))),
            @ApiResponse(responseCode = "401", description = "비밀 번호가 일치하지 않습니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "이미 삭제된 일정입니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDTO.class)))
    })
    public ResponseEntity<TodoDTO> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoForm todoForm) {
        log.info("입력한 todoDTO: {}", todoForm);
        TodoDTO todoDTO = TodoDTO.builder()
                .id(id)
                .title(todoForm.getTitle())
                .content(todoForm.getContent())
                .person(todoForm.getPerson())
                .password(todoForm.getPassword())
                .build();
        Optional<TodoDTO> updateTodo = todoService.updateTodo(todoDTO);
        log.info("수정한 todoDTO: {}", updateTodo);
        if (updateTodo.isPresent()) {
            return ResponseEntity.ok(updateTodo.get());
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
    public String deleteTodo(@PathVariable Long id, @RequestBody Map<String, String> map) {
        String password = map.get("password");
        log.info("입력한 id:{}, password:{}", id, password);
        todoService.deleteTodo(id, password);
        return HttpStatus.OK.toString();
    }
}