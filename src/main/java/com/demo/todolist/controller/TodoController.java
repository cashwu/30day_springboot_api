package com.demo.todolist.controller;

import com.demo.todolist.exceptions.TodoNotFoundException;
import com.demo.todolist.model.MyApiResponse;
import com.demo.todolist.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.demo.todolist.repository.TodoRepository;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @PostMapping
    public ResponseEntity<MyApiResponse<Todo>> createTodo(@RequestBody Todo todo) {

        Todo savedTodo = todoRepository.save(todo);
        return ResponseEntity.ok(new MyApiResponse<>(true, savedTodo, null));
    }

    @GetMapping
    public ResponseEntity<MyApiResponse<List<Todo>>> getAllTodos() {

        List<Todo> todos = (List<Todo>) todoRepository.findAll();
        return ResponseEntity.ok(new MyApiResponse<>(true, todos, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyApiResponse<Todo>> getTodo(@PathVariable Long id) throws TodoNotFoundException {

        return todoRepository.findById(id)
                .map(todo -> ResponseEntity.ok(new MyApiResponse<>(true, todo, null)))
                .orElse(createNotFoundError(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MyApiResponse<Todo>> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {

        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setTitle(updatedTodo.getTitle());
                    todo.setCompleted(updatedTodo.isCompleted());
                    Todo savedTodo = todoRepository.save(todo);
                    return ResponseEntity.ok(new MyApiResponse<>(true, savedTodo, null));
                })
                .orElse(createNotFoundError(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MyApiResponse<Todo>> deleteTodo(@PathVariable Long id) {

        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return ResponseEntity.ok(new MyApiResponse<>(true, null, null));
        } else {
            return createNotFoundError(id);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<MyApiResponse<List<Todo>>> searchTodos(@RequestParam String keyword) {
        List<Todo> todos = todoRepository.findByTitleContainingOrderByIdDesc(keyword);
        return ResponseEntity.ok(new MyApiResponse<>(true, todos, null));
    }

    @GetMapping("/completed")
    public ResponseEntity<MyApiResponse<List<Todo>>> getCompletedTodos() {
        List<Todo> completedTodos = todoRepository.findByCompletedTrue();
        return ResponseEntity.ok(new MyApiResponse<>(true, completedTodos, null));
    }

    @GetMapping("/count-incomplete")
    public ResponseEntity<MyApiResponse<Long>> getIncompleteCount() {
        long count = todoRepository.countByCompletedFalse();
        return ResponseEntity.ok(new MyApiResponse<>(true, count, null));
    }

    @GetMapping("/paged")
    public ResponseEntity<MyApiResponse<Page<Todo>>> getPagedTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Todo> todoPage = todoRepository.findAll(pageable);

        return ResponseEntity.ok(new MyApiResponse<>(true, todoPage, null));
    }

    private ResponseEntity<MyApiResponse<Todo>> createNotFoundError(Long id) {
        MyApiResponse.ErrorDetails error = new MyApiResponse.ErrorDetails(
                "https://example.com/errors/not-found",
                "Todo not found",
                HttpStatus.NOT_FOUND,
                MessageFormat.format("Todo with id {0} does not exist", id),
                MessageFormat.format("/api/todos/{0}", id));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MyApiResponse<>(false, null, error));
    }
}