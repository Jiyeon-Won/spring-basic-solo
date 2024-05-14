package com.sparta.springbasicsolo.controller;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.service.TodoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/")
    public String home(Model model) {
        List<TodoDTO> todoList = todoService.findAll();
        model.addAttribute("items", todoList);
        return "index";
    }

    @GetMapping("/todo")
    public String write() {
        return "write";
    }

    @GetMapping("/todo/{id}")
    public String detail(@PathVariable int id, Model model) {
        Optional<TodoDTO> todoDTO = todoService.findById(id);
        if (todoDTO.isPresent()) {
            model.addAttribute("item", todoDTO);
        }
        return "detail";
    }

    @PostMapping("/todo")
    public String addTodo(TodoDTO todoDTO) {
        Long id = todoService.addTodo(todoDTO);
        log.info("todoDTO: {}", todoDTO);
        return "redirect:/todo/" + id;
    }
}