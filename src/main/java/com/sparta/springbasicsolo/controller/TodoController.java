package com.sparta.springbasicsolo.controller;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.exception.ExceptionDTO;
import com.sparta.springbasicsolo.exception.PasswordNotMatchedException;
import com.sparta.springbasicsolo.service.TodoService;
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
    public TodoDTO addTodo(@RequestBody TodoDTO todoDTO) {
        log.info("입력한 todoDTO: {}", todoDTO);
        Optional<TodoDTO> savedTodo = todoService.addTodo(todoDTO);
        log.info("저장된 todoDTO: {}", todoDTO);

        if (savedTodo.isPresent()) {
            return savedTodo.get();
        }
        return null;
    }

    // 단일 일정 조회
    @GetMapping("/todo/{id}")
    public ResponseEntity<TodoDTO> detail(@PathVariable Long id) {
        Optional<TodoDTO> todoDTO = todoService.findById(id);
        log.info("단일 조회 todoDTO: {}", todoDTO);
        if (todoDTO.isPresent()) {
            return ResponseEntity.ok(todoDTO.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 전체 일정 조회
    @GetMapping("/todo")
    public List<TodoDTO> findAll() {
        List<TodoDTO> todoList = todoService.findAll();
        log.info("전체 조회 todoList: {}", todoList.stream().map(TodoDTO::toString)
                .collect(Collectors.joining(", ", "[", "]")));

        if (!todoList.isEmpty()) {
            return todoList;
        }
        return null;
    }

    // 일정 수정
    @PutMapping("/todo/{id}")
    public ResponseEntity<TodoDTO> updateTodo(@PathVariable Long id, @RequestBody TodoDTO todoDTO) {
        todoDTO.setId(id);
        log.info("입력한 todoDTO: {}", todoDTO);
        Optional<TodoDTO> updateTodo = todoService.updateTodo(todoDTO);
        log.info("수정한 todoDTO: {}", updateTodo);
        if (updateTodo.isPresent()) {
            return ResponseEntity.ok(updateTodo.get());
        }
        return ResponseEntity.notFound().build();
    }

    // 일정 삭제
    @DeleteMapping("/todo/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, @RequestBody Map<String, String> map) {
        String password = map.get("password");
        log.info("입력한 id:{}, password:{}", id, password);
        todoService.deleteTodo(id, password);
        return ResponseEntity.ok().build();
    }
}