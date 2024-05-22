package com.sparta.springbasicsolo.domain.file.repository;

import com.sparta.springbasicsolo.domain.file.repository.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFileRepository extends JpaRepository<Image, Long> {
}