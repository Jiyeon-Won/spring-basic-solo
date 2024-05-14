package com.sparta.springbasicsolo.controller;

import ch.qos.logback.core.model.Model;
import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.service.TodoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/write")
    public String write() {
        return "write";
    }

    @PostMapping("/todo")
    public String addTodo(TodoDTO todoDTO) {
        Long id = todoService.addTodo(todoDTO);
        log.info("todoDTO: {}", todoDTO);
        return "redirect:/detail/" + id;
    }
}