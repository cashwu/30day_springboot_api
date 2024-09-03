package com.demo.todolist.controller;

import com.demo.todolist.exceptions.TodoNotFoundException;
import com.demo.todolist.model.ApiResponse;
import com.demo.todolist.model.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private static final List<Todo> todos = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong();

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @PostMapping
    public ResponseEntity<ApiResponse<Todo>> createTodo(@RequestBody Todo todo) {
        logger.info("create new todo: {}", todo);

        long id = idCounter.incrementAndGet();
        todo.setId(id);
        todos.add(todo);

        return ResponseEntity.ok(new ApiResponse<>(true, todo, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Todo>>> getAllTodos() {
        logger.info("get all todo");

        return ResponseEntity.ok(new ApiResponse<>(true, todos, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Todo>> getTodo(@PathVariable Long id) throws
                                                                            TodoNotFoundException {

        logger.info("get todo by id : {}", id);

        Optional<Todo> todo = todos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();

        if (todo.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(true, todo.get(), null));
        } else {
            throw new TodoNotFoundException(id);
//            return createNotFoundError(id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Todo>> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {

        logger.info("update todo, id : {}, todo : {}", id, updatedTodo);

        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getId().equals(id)) {
                updatedTodo.setId(id);
                todos.set(i, updatedTodo);
                return ResponseEntity.ok(new ApiResponse<>(true, updatedTodo, null));
            }
        }

        return createNotFoundError(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Todo>> deleteTodo(@PathVariable Long id) {

        logger.info("delete todo by id : {}", id);

        boolean isSuccess = todos.removeIf(todo -> todo.getId().equals(id));

        if (isSuccess) {
            return ResponseEntity.ok(new ApiResponse<>(true, null, null));
        }

        return createNotFoundError(id);
    }

    private ResponseEntity<ApiResponse<Todo>> createNotFoundError(Long id) {
        ApiResponse.ErrorDetails error = new ApiResponse.ErrorDetails(
                "https://example.com/errors/not-found",
                "Todo not found",
                HttpStatus.NOT_FOUND,
                MessageFormat.format("Todo with id {0} does not exist", id),
                MessageFormat.format("/api/todos/{0}", id)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, error));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
//        ApiResponse.ErrorDetails error = new ApiResponse.ErrorDetails(
//                "https://example.com/errors/internal-error",
//                "Internal Server Error",
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                e.getMessage(),
//                "/api/todos"
//        );
//        return ResponseEntity.status(500).body(new ApiResponse<>(false, null, error));
//    }
}