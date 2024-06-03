package com.sparta.springbasicsolo.domain.file.repository.entity;

import com.sparta.springbasicsolo.domain.todo.repository.entity.Todo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String extension;
    private long size;
    @Lob
    @Column(columnDefinition = "blob")
    private byte[] data;
    private LocalDateTime uploadDateTime;

    @OneToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;
    private boolean isDeleted;

    @Builder
    public File(String filename, String extension, long size, byte[] data, Todo todo) {
        this.filename = filename;
        this.extension = extension;
        this.size = size;
        this.data = data;
        this.todo = todo;
        uploadDateTime = LocalDateTime.now();
    }
}