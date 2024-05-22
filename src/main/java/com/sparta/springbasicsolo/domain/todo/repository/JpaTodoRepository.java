package com.sparta.springbasicsolo.domain.todo.repository;

import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTodoRepository extends JpaRepository<Todo, Long> {
}