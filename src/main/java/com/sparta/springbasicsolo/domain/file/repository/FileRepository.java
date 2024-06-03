package com.sparta.springbasicsolo.domain.file.repository;

import com.sparta.springbasicsolo.domain.file.repository.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}