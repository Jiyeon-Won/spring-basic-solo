package com.sparta.springbasicsolo.domain.user.repository;

import com.sparta.springbasicsolo.domain.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}