package com.sparta.springbasicsolo.repository;

import com.sparta.springbasicsolo.TodoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcTodoRepository implements TodoRepository {

    private final NamedParameterJdbcTemplate template;

    @Override
    public Long addTodo(TodoDTO todoDTO) {
        todoDTO.setCreatedDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        String sql = "INSERT INTO todo (title, content, person, password, created_date_time)"
                + " VALUES (:title, :content, :person, :password, :created_date_time)";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("title", todoDTO.getTitle())
                .addValue("content", todoDTO.getContent())
                .addValue("person", todoDTO.getPerson())
                .addValue("password", todoDTO.getPassword())
                .addValue("created_date_time", todoDTO.getCreatedDateTime());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        todoDTO.setId(key);
        return key;
    }

    @Override
    public Optional<TodoDTO> findById(int id) {
        String sql = "SELECT * FROM todo WHERE id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);

        TodoDTO todoDTO = template.queryForObject(sql, param, getTodoDTORowMapper());
        if (todoDTO != null) {
            return Optional.of(todoDTO);
        }
        return Optional.empty();
    }

    @Override
    public List<TodoDTO> findAll() {
        String sql = "SELECT * FROM todo";

        return template.query(sql, getTodoDTORowMapper());
    }

    private BeanPropertyRowMapper<TodoDTO> getTodoDTORowMapper() {
        return new BeanPropertyRowMapper<>(TodoDTO.class);
    }
}