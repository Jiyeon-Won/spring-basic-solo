package com.sparta.springbasicsolo.controller;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.service.TodoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/")
    public String home() {
        return "index";
    }
}