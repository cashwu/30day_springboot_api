package com.demo.todolist.controller;

import com.demo.todolist.model.Todo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private static final List<Todo> todos = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong();

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        long id = idCounter.incrementAndGet();
        todo.setId(id);
        todos.add(todo);
        return todo;
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todos;
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable Long id) {
        return todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getId().equals(id)) {
                updatedTodo.setId(id);
                todos.set(i, updatedTodo);
                return updatedTodo;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todos.removeIf(todo -> todo.getId().equals(id));
    }
}
