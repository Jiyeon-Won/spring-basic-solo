package com.sparta.springbasicsolo.repository;

import com.sparta.springbasicsolo.TodoDTO;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    Long addTodo(TodoDTO todoDTO);

    Optional<TodoDTO> findById(int id);

    List<TodoDTO> findAll();
}