package com.sparta.springbasicsolo.service;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.repository.JdbcTodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final JdbcTodoRepository todoRepository;

    public Optional<TodoDTO> addTodo(TodoDTO todoDTO) {
        return todoRepository.addTodo(todoDTO);
    }

    public Optional<TodoDTO> findById(Long id) {
        return todoRepository.findById(id);
    }

    public List<TodoDTO> findAll() {
        return todoRepository.findAll();
    }
}