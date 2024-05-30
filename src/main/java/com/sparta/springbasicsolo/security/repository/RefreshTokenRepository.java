package com.sparta.springbasicsolo.security.repository;

import com.sparta.springbasicsolo.security.repository.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}