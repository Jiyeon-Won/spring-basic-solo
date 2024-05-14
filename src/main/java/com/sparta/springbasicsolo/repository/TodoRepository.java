package com.sparta.springbasicsolo.repository;

import com.sparta.springbasicsolo.TodoDTO;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    Optional<TodoDTO> addTodo(TodoDTO todoDTO);

    Optional<TodoDTO> findById(Long id);

    List<TodoDTO> findAll();

    int updateTodo(TodoDTO todoDTO);
}