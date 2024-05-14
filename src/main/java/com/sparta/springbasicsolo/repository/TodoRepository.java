package com.sparta.springbasicsolo.repository;

import com.sparta.springbasicsolo.TodoDTO;

public interface TodoRepository {
    Long addTodo(TodoDTO todoDTO);
}