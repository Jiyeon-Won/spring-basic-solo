package com.sparta.springbasicsolo.service;

import com.sparta.springbasicsolo.controller.dto.TodoRequestDTO;
import com.sparta.springbasicsolo.exception.DeletedTodoException;
import com.sparta.springbasicsolo.exception.PasswordNotMatchedException;
import com.sparta.springbasicsolo.repository.JpaTodoRepository;
import com.sparta.springbasicsolo.repository.entity.Todo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final JpaTodoRepository todoRepository;

    public Optional<Todo> addTodo(TodoRequestDTO dto) {
        Todo todo = dto.toEntity();
        return Optional.of(todoRepository.save(todo));
    }

    public Optional<Todo> findById(Long id) {
        Optional<Todo> findTodo = todoRepository.findById(id);
        findTodo.ifPresent(todo -> ifDeletedThrow(todo.getIsDeleted()));
        return findTodo;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Optional<Todo> updateTodo(Long id, TodoRequestDTO dto) {
        Optional<Todo> findTodo = todoRepository.findById(id);
        if (findTodo.isPresent()) {
            ifDeletedThrow(findTodo.get().getIsDeleted());
            ifPasswordNotMatchedThrow(Objects.equals(findTodo.get().getPassword(), dto.getPassword()));
            Todo updateTodo = findTodo.get();
            updateTodo.setTitle(dto.getTitle());
            updateTodo.setContent(dto.getContent());
            updateTodo.setPerson(dto.getPerson());
            return Optional.of(todoRepository.save(updateTodo));
        }
        return Optional.empty();
    }

    public void deleteTodo(Long id, String password) {
        Optional<Todo> findTodo = todoRepository.findById(id);
        if (findTodo.isPresent()) {
            ifDeletedThrow(findTodo.get().getIsDeleted());
            ifPasswordNotMatchedThrow(Objects.equals(findTodo.get().getPassword(), password));
            Todo updateTodo = findTodo.get();
            updateTodo.setIsDeleted(true);
            todoRepository.save(updateTodo);
        }
    }

    private void ifPasswordNotMatchedThrow(boolean isPasswordNotMatched) {
        if (!isPasswordNotMatched) {
            throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void ifDeletedThrow(boolean isDeleted) {
        if (isDeleted) {
            throw new DeletedTodoException("이미 삭제된 일정입니다.");
        }
    }
}