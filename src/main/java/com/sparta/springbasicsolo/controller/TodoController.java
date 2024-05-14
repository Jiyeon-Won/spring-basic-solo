package com.sparta.springbasicsolo.controller;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.service.TodoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public TodoDTO detail(@PathVariable Long id) {
        Optional<TodoDTO> todoDTO = todoService.findById(id);
        log.info("단일 조회 todoDTO: {}", todoDTO);
        if (todoDTO.isPresent()) {
            return todoDTO.get();
        }
        return null;
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
}