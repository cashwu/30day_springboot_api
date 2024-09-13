package com.demo.todolist.controller;

import com.demo.todolist.model.Todo;
import com.demo.todolist.services.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author cash.wu
 * @since 2024/09/10
 */
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user")
    public void createTodo() throws Exception {
        Todo todo = new Todo(null, "新待辦事項", false);
        Todo savedTodo = new Todo(1L, "新待辦事項", false);

        when(todoService.save(any(Todo.class))).thenReturn(savedTodo);

        mockMvc.perform(post("/api/todos")
                                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
               .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.data.id").value(1))
               .andExpect(jsonPath("$.data.title").value("新待辦事項"))
               .andExpect(jsonPath("$.data.completed").value(false));

        verify(todoService, times(1)).save(any(Todo.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void getAllTodos() throws Exception {
        Todo todo1 = new Todo(1L, "測試待辦事項", false);
        Todo todo2 = new Todo(2L, "測試待辦事項2", true);

        when(todoService.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        mockMvc.perform(get("/api/todos")).andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.data[0].id").value(1))
               .andExpect(jsonPath("$.data[0].title").value("測試待辦事項"))
               .andExpect(jsonPath("$.data[0].completed").value(false))
               .andExpect(jsonPath("$.data[1].id").value(2))
               .andExpect(jsonPath("$.data[1].title").value("測試待辦事項2"))
               .andExpect(jsonPath("$.data[1].completed").value(true));

        verify(todoService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "user")
    public void getTodo() throws Exception {
        Todo todo = new Todo(1L, "測試待辦事項", false);

        when(todoService.findById(1L)).thenReturn(Optional.of(todo));

        mockMvc.perform(get("/api/todos/1")).andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.data.id").value(1))
               .andExpect(jsonPath("$.data.title").value("測試待辦事項"))
               .andExpect(jsonPath("$.data.completed").value(false));

        verify(todoService, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(username = "user")
    public void updateTodo() throws Exception {
        Todo updatedTodo = new Todo(1L, "更新的待辦事項", true);

        when(todoService.updateTodo(eq(1L), any(Todo.class))).thenReturn(Optional.of(updatedTodo));

        mockMvc.perform(put("/api/todos/1")
                                .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedTodo)))
               .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.data.id").value(1))
               .andExpect(jsonPath("$.data.title").value("更新的待辦事項"))
               .andExpect(jsonPath("$.data.completed").value(true));

        verify(todoService, times(1)).updateTodo(eq(1L), any(Todo.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void deleteTodo() throws Exception {
        when(todoService.deleteTodo(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/todos/1")
                                .with(csrf()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true));

        verify(todoService, times(1)).deleteTodo(1L);
    }
}