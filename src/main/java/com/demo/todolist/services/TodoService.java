package com.demo.todolist.services;

import com.demo.todolist.model.Todo;
import com.demo.todolist.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public Todo save(Todo todo) {

        // 保存 Todo
        Todo savedTodo = todoRepository.save(todo);

        // 假設我們需要進行一些額外的操作
        // 如果這裡拋出異常，整個交易會回滾
        performAdditionalOperations(savedTodo);

        return savedTodo;
    }

    @Transactional(readOnly = true)
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    private void performAdditionalOperations(Todo todo) {
        // 模擬一些可能會拋出異常的操作
        if (todo.getTitle().length() > 20) {
            throw new RuntimeException("Todo title is too long from TodoService");
        }
    }
}
