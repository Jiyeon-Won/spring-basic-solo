package com.sparta.springbasicsolo.service;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.repository.JdbcTodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final JdbcTodoRepository todoRepository;
}