package com.sparta.springbasicsolo.domain.todo.service;

import com.sparta.springbasicsolo.domain.todo.dto.TodoRequestDTO;
import com.sparta.springbasicsolo.domain.todo.repository.JpaTodoRepository;
import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import com.sparta.springbasicsolo.domain.user.repository.entity.User;
import com.sparta.springbasicsolo.exception.DeletedTodoException;
import com.sparta.springbasicsolo.exception.PasswordNotMatchedException;
import com.sparta.springbasicsolo.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final JpaTodoRepository todoRepository;

    @Transactional
    public Todo addTodo(TodoRequestDTO dto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if (!dto.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("로그인한 username과 추가할 username 다르다.");
        }
        Todo todo = Todo.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .password(dto.getPassword())
                .build();

        return todoRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public Todo findById(Long id) {
        Todo findTodo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 리소스를 찾을 수 없습니다."));
        ifDeletedThrow(findTodo.getIsDeleted());
        return findTodo;
    }

    @Transactional(readOnly = true)
    public List<Todo> findAll() {
        List<Todo> todos = todoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        if (todos.isEmpty()) {
            throw new IllegalArgumentException("요청하신 리소스를 찾을 수 없습니다.");
        }
        return todos.stream().filter(todo -> !todo.getIsDeleted()).toList();
    }

    @Transactional
    public Todo updateTodo(Long todoId, TodoRequestDTO dto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if (!user.getUsername().equals(dto.getUsername())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        Todo findTodo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 리소스를 찾을 수 없습니다."));
        ifDeletedThrow(findTodo.getIsDeleted());
        ifPasswordNotMatchedThrow(Objects.equals(findTodo.getPassword(), dto.getPassword()));

        findTodo.setTitle(dto.getTitle());
        findTodo.setContent(dto.getContent());
        findTodo.setUser(user);
        return todoRepository.save(findTodo);
    }

    @Transactional
    public void deleteTodo(Long todoId, Long userId, String password, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if (!Objects.equals(user.getId(), userId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        Todo findTodo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 리소스를 찾을 수 없습니다."));
        ifDeletedThrow(findTodo.getIsDeleted());
        ifPasswordNotMatchedThrow(Objects.equals(findTodo.getPassword(), password));

        if (!password.equals(findTodo.getPassword())) {
            throw new IllegalArgumentException("password 다르다");
        }

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