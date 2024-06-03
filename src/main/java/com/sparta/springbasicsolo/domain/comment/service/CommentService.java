package com.sparta.springbasicsolo.domain.comment.service;

import com.sparta.springbasicsolo.domain.comment.dto.CommentActionDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentDeleteDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentRequestDTO;
import com.sparta.springbasicsolo.domain.comment.dto.CommentResponseDTO;
import com.sparta.springbasicsolo.domain.comment.repository.CommentRepository;
import com.sparta.springbasicsolo.domain.comment.repository.entity.Comment;
import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import com.sparta.springbasicsolo.domain.todo.service.TodoService;
import com.sparta.springbasicsolo.domain.user.repository.entity.User;
import com.sparta.springbasicsolo.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final TodoService todoService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDTO saveComment(CommentRequestDTO dto, UserDetailsImpl userDetails) {
        Todo findTodo = todoService.findById(dto.getTodoId());

        User loginUser = userDetails.getUser();

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .user(loginUser)
                .todo(findTodo)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDTO(
                savedComment.getContent(),
                savedComment.getUser().getId(),
                savedComment.getId(),
                savedComment.getCreatedDateTime()
        );
    }

    @Transactional
    public CommentResponseDTO updateComment(CommentActionDTO dto, UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        if (!Objects.equals(loginUser.getId(), dto.getUserId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        Todo findTodo = todoService.findById(dto.getTodoId());
        Comment findComment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 댓글 번호입니다."));

        if (!Objects.equals(findComment.getUser().getId(), loginUser.getId())) {
            throw new IllegalArgumentException("선택한 댓글의 사용자가 현재 사용자와 일치하지 않습니다.");
        }

        findComment.setContent(dto.getContent());
        findComment.setUpdatedDateTime(LocalDateTime.now());

        Comment updatedComment = commentRepository.save(findComment);

        return new CommentResponseDTO(
                updatedComment.getContent(),
                updatedComment.getId(),
                updatedComment.getCreatedDateTime()
        );
    }

    @Transactional
    public void deleteComment(CommentDeleteDTO dto, UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        if (!Objects.equals(loginUser.getId(), dto.getUserId())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        Todo findTodo = todoService.findById(dto.getTodoId());
        Comment findComment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 댓글 번호입니다."));

        if (!Objects.equals(findComment.getUser().getId(), loginUser.getId())) {
            throw new IllegalArgumentException("선택한 댓글의 사용자가 현재 사용자와 일치하지 않습니다.");
        }

        commentRepository.delete(findComment);
    }
}