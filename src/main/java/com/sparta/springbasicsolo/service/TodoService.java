package com.sparta.springbasicsolo.service;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.exception.DeletedTodoException;
import com.sparta.springbasicsolo.exception.PasswordNotMatchedException;
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
        Optional<TodoDTO> todoDTO = todoRepository.findById(id);
        if (todoDTO.isPresent()) {
            if (todoDTO.get().getIsDeleted()) {
                throw new DeletedTodoException("이미 삭제된 일정입니다.");
            }
        }
        return todoDTO;
    }

    public List<TodoDTO> findAll() {
        return todoRepository.findAll();
    }

    public Optional<TodoDTO> updateTodo(TodoDTO todoDTO) {
        Optional<TodoDTO> findTodo = todoRepository.findById(todoDTO.getId());
        if (findTodo.isPresent()) {
            if (Objects.equals(findTodo.get().getPassword(), todoDTO.getPassword())) {
                log.info("비밀번호 일치함. 업데이트 진행");
                int updatedRowCount = todoRepository.updateTodo(todoDTO);
                if (updatedRowCount == 1) {
                    return todoRepository.findById(todoDTO.getId());
                }
            } else {
                throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다.");
            }
        }
        return Optional.empty();
    }

    public void deleteTodo(Long id, String password) {
        Optional<TodoDTO> findTodo = todoRepository.findById(id);
        if (findTodo.isPresent()) {
            if (findTodo.get().getIsDeleted()) {
                throw new DeletedTodoException("이미 삭제된 일정입니다.");
            }
            if (Objects.equals(findTodo.get().getPassword(), password)) {
                log.info("비밀번호 일치함. 삭제 진행");
                todoRepository.deleteTodo(id);
            } else {
                throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다.");
            }
        }
    }
}