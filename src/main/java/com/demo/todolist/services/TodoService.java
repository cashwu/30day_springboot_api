package com.demo.todolist.services;

import com.demo.todolist.model.Todo;
import com.demo.todolist.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public Todo save(Todo todo) {
        Todo savedTodo = todoRepository.save(todo);
        performAdditionalOperations(savedTodo);
        return savedTodo;
    }

    @Transactional(readOnly = true)
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    @Transactional
    public Optional<Todo> updateTodo(Long id, Todo updatedTodo) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setTitle(updatedTodo.getTitle());
                    todo.setCompleted(updatedTodo.isCompleted());
                    return todoRepository.save(todo);
                });
    }

    @Transactional
    public boolean deleteTodo(Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<Todo> searchByKeyword(String keyword) {
        return todoRepository.findByTitleContainingOrderByIdDesc(keyword);
    }

    @Transactional(readOnly = true)
    public List<Todo> findCompletedTodos() {
        return todoRepository.findByCompletedTrue();
    }

    @Transactional(readOnly = true)
    public long countIncompleteTodos() {
        return todoRepository.countByCompletedFalse();
    }

    @Transactional(readOnly = true)
    public Page<Todo> getPagedTodos(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return todoRepository.findAll(pageable);
    }

    private void performAdditionalOperations(Todo todo) {
        if (todo.getTitle().length() > 20) {
            throw new RuntimeException("Todo title is too long from TodoService");
        }
    }
}
