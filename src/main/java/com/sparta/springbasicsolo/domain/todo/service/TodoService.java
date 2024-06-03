package com.sparta.springbasicsolo.domain.todo.service;

import com.sparta.springbasicsolo.domain.file.repository.FileRepository;
import com.sparta.springbasicsolo.domain.file.repository.entity.File;
import com.sparta.springbasicsolo.domain.todo.dto.TodoRequestDTO;
import com.sparta.springbasicsolo.domain.todo.repository.JpaTodoRepository;
import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import com.sparta.springbasicsolo.domain.user.repository.entity.User;
import com.sparta.springbasicsolo.exception.DeletedTodoException;
import com.sparta.springbasicsolo.security.service.UserDetailsImpl;
import com.sparta.springbasicsolo.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final JpaTodoRepository todoRepository;
    private final FileRepository fileRepository;

    @Transactional
    public Todo addTodo(TodoRequestDTO dto, MultipartFile file, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if (!dto.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("로그인한 username과 추가할 username 다르다.");
        }

        Todo todo = Todo.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();

        if (file != null) {
            FileUtils.invalidExtension(file.getOriginalFilename(), "png", "jpg");
            FileUtils.invalidFileSize(file.getSize(), 5);

            File uploadFile;
            try {
                uploadFile = File.builder()
                        .filename(FileUtils.extractOriginalName(file.getOriginalFilename()))
                        .extension(FileUtils.extractExtension(file.getOriginalFilename()))
                        .size(file.getSize())
                        .data(file.getBytes())
                        .todo(todo)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e);
            }

            todo.setFile(uploadFile);
        }

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
    public Todo updateTodo(Long todoId, TodoRequestDTO dto, MultipartFile file, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if (!user.getUsername().equals(dto.getUsername())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        Todo findTodo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 리소스를 찾을 수 없습니다."));
        ifDeletedThrow(findTodo.getIsDeleted());

        findTodo.setTitle(dto.getTitle());
        findTodo.setContent(dto.getContent());
        findTodo.setUser(user);
        findTodo.setUpdatedDateTime(LocalDateTime.now());

        if (file != null) {
            FileUtils.invalidExtension(file.getOriginalFilename(), "png", "jpg");
            FileUtils.invalidFileSize(file.getSize(), 5);

            File uploadFile = findTodo.getFile();
            try {
                uploadFile.setFilename(FileUtils.extractOriginalName(file.getOriginalFilename()));
                uploadFile.setExtension(FileUtils.extractExtension(file.getOriginalFilename()));
                uploadFile.setSize(file.getSize());
                uploadFile.setData(file.getBytes());
                uploadFile.setTodo(findTodo);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e);
            }
            findTodo.setFile(uploadFile);
        }

        return todoRepository.save(findTodo);
    }

    @Transactional
    public void deleteTodo(Long todoId, Long userId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if (!Objects.equals(user.getId(), userId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        Todo findTodo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 리소스를 찾을 수 없습니다."));
        ifDeletedThrow(findTodo.getIsDeleted());

        findTodo.setIsDeleted(true);
        findTodo.getFile().setDeleted(true);
        todoRepository.save(findTodo);
    }

    private void ifDeletedThrow(boolean isDeleted) {
        if (isDeleted) {
            throw new DeletedTodoException("이미 삭제된 일정입니다.");
        }
    }
}