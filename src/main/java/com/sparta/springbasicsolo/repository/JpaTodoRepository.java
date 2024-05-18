package com.sparta.springbasicsolo.repository;

import com.sparta.springbasicsolo.TodoDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTodoRepository extends JpaRepository<TodoDTO, Long> {
}