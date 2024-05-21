package com.sparta.springbasicsolo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springbasicsolo.controller.tododto.TodoRequestDTO;
import com.sparta.springbasicsolo.controller.tododto.TodoResponseDTO;
import com.sparta.springbasicsolo.repository.JpaTodoRepository;
import com.sparta.springbasicsolo.repository.entity.Todo;
import com.sparta.springbasicsolo.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    @DisplayName("Todo 생성")
    void addTodo() throws Exception {
        Todo todo = new Todo("테스트_제목", "테스트_내용", "test@naver.com", "test");

        TodoRequestDTO todoRequestDTO = new TodoRequestDTO();
        todoRequestDTO.setTitle("테스트_제목");
        todoRequestDTO.setContent("테스트_내용");
        todoRequestDTO.setPerson("test@naver.com");
        todoRequestDTO.setPassword("test");

        given(todoService.addTodo(todoRequestDTO))
                .willReturn(Optional.of(todo));

        String toJson = new ObjectMapper().writeValueAsString(todoRequestDTO);

        mockMvc.perform(
                        post("/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson))
                .andExpect(status().isOk())
                .andDo(print());

        verify(todoService).addTodo(todoRequestDTO);
    }

    @Test
    void detail() {
    }

    @Test
    void findAll() {
    }

    @Test
    void updateTodo() {
    }

    @Test
    void deleteTodo() {
    }
}