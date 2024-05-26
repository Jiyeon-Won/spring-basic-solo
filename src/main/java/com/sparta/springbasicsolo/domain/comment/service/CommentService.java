package com.sparta.springbasicsolo.domain.comment.service;

import com.sparta.springbasicsolo.domain.comment.dto.CommentRequestDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentResponseDTO;
import com.sparta.springbasicsolo.domain.comment.repository.CommentRepository;
import com.sparta.springbasicsolo.domain.comment.repository.entity.Comment;
import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import com.sparta.springbasicsolo.domain.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final TodoService todoService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDTO saveComment(CommentRequestDTO dto) {
        Todo todo = todoService.findById(dto.getTodoId());

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .userId(dto.getUserId())
                .todo(todo)
                .build();

        Comment savedComment;
        try {
            savedComment = commentRepository.save(comment);
        } catch (DataAccessException e) {
            throw new RuntimeException("댓글 저장 실패했습니다.", e);
        }

        return new CommentResponseDTO(savedComment);
    }
}