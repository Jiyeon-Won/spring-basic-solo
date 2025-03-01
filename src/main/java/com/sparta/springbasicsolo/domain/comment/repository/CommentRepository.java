package com.sparta.springbasicsolo.domain.comment.repository;

import com.sparta.springbasicsolo.domain.comment.repository.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}