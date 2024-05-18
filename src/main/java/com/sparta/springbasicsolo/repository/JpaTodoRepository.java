package com.sparta.springbasicsolo.repository;

import com.sparta.springbasicsolo.repository.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTodoRepository extends JpaRepository<Todo, Long> {
}