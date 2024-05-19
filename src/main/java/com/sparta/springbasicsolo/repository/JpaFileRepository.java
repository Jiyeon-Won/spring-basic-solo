package com.sparta.springbasicsolo.repository;

import com.sparta.springbasicsolo.repository.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFileRepository extends JpaRepository<Image, Long> {
}