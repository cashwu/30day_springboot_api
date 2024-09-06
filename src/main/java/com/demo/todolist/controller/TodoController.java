package com.demo.todolist.controller;

import com.demo.todolist.exceptions.TodoNotFoundException;
import com.demo.todolist.model.MyApiResponse;
import com.demo.todolist.model.Todo;
import com.demo.todolist.model.TodoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todo", description = "待辦事項管理 API")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    private final JdbcClient jdbcClient;
    private final TodoValidator todoValidator;

    public TodoController(JdbcClient jdbcClient, TodoValidator todoValidator) {
        this.jdbcClient = jdbcClient;
        this.todoValidator = todoValidator;
    }

    @PostMapping
    @Operation(summary = "建立新的待辦事項", description = "建立一個新的待辦事項並將其加入數據庫中")
    public ResponseEntity<MyApiResponse<Todo>> createTodo(
            @Parameter(description = "待辦事項") @Valid @RequestBody Todo todo,
            BindingResult bindingResult) {

        logger.info("create new todo: {}", todo);

        todoValidator.validate(todo, bindingResult);

        if (bindingResult.hasErrors()) {
            MyApiResponse.ErrorDetails error = new MyApiResponse.ErrorDetails(
                    "https://example.com/errors/validation-error",
                    "Validation Error",
                    HttpStatus.BAD_REQUEST,
                    "There were validation errors",
                    "/api/todos");

            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            error.setValidationErrors(errorMessages);

            return ResponseEntity.badRequest().body(new MyApiResponse<>(false, null, error));
        }

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcClient.sql("INSERT INTO todos (title, completed) VALUES (:title, :completed)")
                    .param("title", todo.getTitle())
                    .param("completed", todo.isCompleted())
                    .update(keyHolder);

            Long id = keyHolder.getKey().longValue();

            todo.setId(id);
            return ResponseEntity.ok(new MyApiResponse<>(true, todo, null));
        } catch (Exception e) {
            logger.error("創建待辦事項失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyApiResponse<>(false, null, new MyApiResponse.ErrorDetails(
                            "https://example.com/errors/database-error",
                            "Database Error",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "無法創建待辦事項",
                            "/api/todos")));
        }
    }

    @GetMapping
    @Operation(summary = "取得所有待辦事項", description = "取得所有待辦事項的清單")
    public ResponseEntity<MyApiResponse<List<Todo>>> getAllTodos() {
        logger.info("get all todo");

        try {
            List<Todo> todos = jdbcClient.sql("SELECT * FROM todos")
                    .query(Todo.class)
                    .list();
            return ResponseEntity.ok(new MyApiResponse<>(true, todos, null));
        } catch (Exception e) {
            logger.error("獲取所有待辦事項失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyApiResponse<>(false, null, new MyApiResponse.ErrorDetails(
                            "https://example.com/errors/database-error",
                            "Database Error",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "無法獲取待辦事項列表",
                            "/api/todos")));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "取得單一待辦事項", description = "根據ID取得單一待辦事項的詳細資訊")
    public ResponseEntity<MyApiResponse<Todo>> getTodo(@PathVariable Long id) throws TodoNotFoundException {
        logger.info("get todo by id : {}", id);

        try {
            Todo todo = jdbcClient.sql("SELECT * FROM todos WHERE id = :id")
                    .param("id", id)
                    .query(Todo.class)
                    .optional()
                    .orElse(null);

            if (todo == null) {
                return createNotFoundError(id);
            }

            return ResponseEntity.ok(new MyApiResponse<>(true, todo, null));
        } catch (Exception e) {
            logger.error("獲取待辦事項失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyApiResponse<>(false, null, new MyApiResponse.ErrorDetails(
                            "https://example.com/errors/database-error",
                            "Database Error",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "無法獲取待辦事項",
                            "/api/todos/" + id)));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新待辦事項", description = "根據ID更新一個已存在的待辦事項")
    public ResponseEntity<MyApiResponse<Todo>> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        logger.info("update todo, id : {}, todo : {}", id, updatedTodo);

        try {
            int affectedRows = jdbcClient.sql("UPDATE todos SET title = :title, completed = :completed WHERE id = :id")
                    .param("title", updatedTodo.getTitle())
                    .param("completed", updatedTodo.isCompleted())
                    .param("id", id)
                    .update();

            if (affectedRows > 0) {
                updatedTodo.setId(id);
                return ResponseEntity.ok(new MyApiResponse<>(true, updatedTodo, null));
            } else {
                return createNotFoundError(id);
            }
        } catch (Exception e) {
            logger.error("更新待辦事項失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyApiResponse<>(false, null, new MyApiResponse.ErrorDetails(
                            "https://example.com/errors/database-error",
                            "Database Error",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "無法更新待辦事項",
                            "/api/todos/" + id)));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除待辦事項", description = "根據ID刪除一個待辦事項")
    public ResponseEntity<MyApiResponse<Todo>> deleteTodo(@PathVariable Long id) {
        logger.info("delete todo by id : {}", id);

        try {
            int affectedRows = jdbcClient.sql("DELETE FROM todos WHERE id = :id")
                    .param("id", id)
                    .update();

            if (affectedRows > 0) {
                return ResponseEntity.ok(new MyApiResponse<>(true, null, null));
            } else {
                return createNotFoundError(id);
            }
        } catch (Exception e) {
            logger.error("刪除待辦事項失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyApiResponse<>(false, null, new MyApiResponse.ErrorDetails(
                            "https://example.com/errors/database-error",
                            "Database Error",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "無法刪除待辦事項",
                            "/api/todos/" + id)));
        }
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