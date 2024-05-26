package com.sparta.springbasicsolo.domain.todo.service;

import com.sparta.springbasicsolo.domain.todo.dto.TodoRequestDTO;
import com.sparta.springbasicsolo.exception.DeletedTodoException;
import com.sparta.springbasicsolo.exception.PasswordNotMatchedException;
import com.sparta.springbasicsolo.domain.todo.repository.JpaTodoRepository;
import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final JpaTodoRepository todoRepository;

    @Transactional
    public Todo addTodo(TodoRequestDTO dto) {
        Todo todo = dto.toEntity();
        return todoRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public Todo findById(Long id) {
        Todo findTodo = todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("요청하신 리소스를 찾을 수 없습니다."));
        ifDeletedThrow(findTodo.getIsDeleted());
        return findTodo;
    }

    @Transactional(readOnly = true)
    public List<Todo> findAll() {
        List<Todo> todos = todoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        if (todos.isEmpty()) {
            throw new NoSuchElementException("요청하신 리소스를 찾을 수 없습니다.");
        }
        return todos;
    }

    @Transactional
    public Todo updateTodo(Long id, TodoRequestDTO dto) {
        Todo findTodo = todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("요청하신 리소스를 찾을 수 없습니다."));
        ifDeletedThrow(findTodo.getIsDeleted());
        ifPasswordNotMatchedThrow(Objects.equals(findTodo.getPassword(), dto.getPassword()));

        findTodo.setTitle(dto.getTitle());
        findTodo.setContent(dto.getContent());
        findTodo.setPerson(dto.getPerson());
        return todoRepository.save(findTodo);
    }

    @Transactional
    public void deleteTodo(Long id, String password) {
        Todo findTodo = todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("요청하신 리소스를 찾을 수 없습니다."));
        ifDeletedThrow(findTodo.getIsDeleted());
        ifPasswordNotMatchedThrow(Objects.equals(findTodo.getPassword(), password));

        findTodo.setIsDeleted(true);
        todoRepository.save(findTodo);
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