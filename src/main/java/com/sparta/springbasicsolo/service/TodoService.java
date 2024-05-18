package com.sparta.springbasicsolo.service;

import com.sparta.springbasicsolo.TodoDTO;
import com.sparta.springbasicsolo.exception.DeletedTodoException;
import com.sparta.springbasicsolo.exception.PasswordNotMatchedException;
import com.sparta.springbasicsolo.repository.JpaTodoRepository;
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

    //    private final JdbcTodoRepository todoRepository;
    private final JpaTodoRepository todoRepository;

    public Optional<TodoDTO> addTodo(TodoDTO todoDTO) {
//        return todoRepository.addTodo(todoDTO);
        return Optional.of(todoRepository.save(todoDTO));
    }

    public Optional<TodoDTO> findById(Long id) {
//        Optional<TodoDTO> todoDTO = todoRepository.findById(id);
//        if (todoDTO.isPresent()) {
//            if (todoDTO.get().getIsDeleted()) {
//                throw new DeletedTodoException("이미 삭제된 일정입니다.");
//            }
//        }
//        return todoDTO;
        Optional<TodoDTO> todoDTO = todoRepository.findById(id);
        todoDTO.ifPresent(todo -> ifDeletedThrow(todo.getIsDeleted()));
        return todoDTO;
    }

    public List<TodoDTO> findAll() {
        return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Optional<TodoDTO> updateTodo(TodoDTO todoDTO) {
//        Optional<TodoDTO> findTodo = todoRepository.findById(todoDTO.getId());
//        if (findTodo.isPresent()) {
//            if (findTodo.get().getIsDeleted()) {
//                throw new DeletedTodoException("이미 삭제된 일정입니다.");
//            }
//            if (Objects.equals(findTodo.get().getPassword(), todoDTO.getPassword())) {
//                log.info("비밀번호 일치함. 업데이트 진행");
//                int updatedRowCount = todoRepository.updateTodo(todoDTO);
//                if (updatedRowCount == 1) {
//                    return todoRepository.findById(todoDTO.getId());
//                }
//            } else {
//                throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다.");
//            }
//        }
//        return Optional.empty();
        Optional<TodoDTO> findTodo = todoRepository.findById(todoDTO.getId());
        if (findTodo.isPresent()) {
            ifDeletedThrow(findTodo.get().getIsDeleted());
            ifPasswordNotMatchedThrow(Objects.equals(findTodo.get().getPassword(), todoDTO.getPassword()));
            TodoDTO updateTodo = findTodo.get();
            updateTodo.setTitle(todoDTO.getTitle());
            updateTodo.setContent(todoDTO.getContent());
            updateTodo.setPerson(todoDTO.getPerson());
            updateTodo.setPassword(todoDTO.getPassword());
            return Optional.of(todoRepository.save(updateTodo));
        }
        return Optional.empty();
    }

    public void deleteTodo(Long id, String password) {
//        Optional<TodoDTO> findTodo = todoRepository.findById(id);
//        if (findTodo.isPresent()) {
//            if (findTodo.get().getIsDeleted()) {
//                throw new DeletedTodoException("이미 삭제된 일정입니다.");
//            }
//            if (Objects.equals(findTodo.get().getPassword(), password)) {
//                log.info("비밀번호 일치함. 삭제 진행");
//                todoRepository.deleteTodo(id);
//            } else {
//                throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다.");
//            }
//        }
        Optional<TodoDTO> findTodo = todoRepository.findById(id);
        if (findTodo.isPresent()) {
            ifDeletedThrow(findTodo.get().getIsDeleted());
            ifPasswordNotMatchedThrow(Objects.equals(findTodo.get().getPassword(), password));
            TodoDTO updateTodo = findTodo.get();
            updateTodo.setIsDeleted(true);
            log.info("바뀐 todo = |{}|", updateTodo);
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