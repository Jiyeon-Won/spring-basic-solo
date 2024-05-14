package com.sparta.springbasicsolo.service;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.repository.JdbcTodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public Optional<TodoDTO> updateTodo(TodoDTO todoDTO) {
        Optional<TodoDTO> findTodo = todoRepository.findById(todoDTO.getId());
        if (findTodo.isPresent()) {
            if (Objects.equals(findTodo.get().getPassword(), todoDTO.getPassword())) {
                log.info("비밀번호 일치함. 업데이트 진행");
                int updatedColumnCount = todoRepository.updateTodo(todoDTO);
                if (updatedColumnCount == 1) {
                    return todoRepository.findById(todoDTO.getId());
                }
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}