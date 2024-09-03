package com.demo.todolist.controller;

import com.demo.todolist.exceptions.TodoNotFoundException;
import com.demo.todolist.model.MyApiResponse;
import com.demo.todolist.model.Todo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Todo", description = "待辦事項管理 API")
public class TodoController {

    private static final List<Todo> todos = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong();

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @PostMapping
    @Operation(summary = "建立新的待辦事項", description = "建立一個新的待辦事項並將其加入清單中",
    responses = {
            @ApiResponse(responseCode = "200", description = "成功創建待辦事項",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "無效的輸入")
    })
    public ResponseEntity<MyApiResponse<Todo>> createTodo(
            @Parameter(description = "待辦事項")
            @RequestBody Todo todo) {
        logger.info("create new todo: {}", todo);

        long id = idCounter.incrementAndGet();
        todo.setId(id);
        todos.add(todo);

        return ResponseEntity.ok(new MyApiResponse<>(true, todo, null));
    }

    @GetMapping
    @Operation(summary = "取得所有待辦事項", description = "取得所有待辦事項的清單")
    public ResponseEntity<MyApiResponse<List<Todo>>> getAllTodos() {
        logger.info("get all todo");

        return ResponseEntity.ok(new MyApiResponse<>(true, todos, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "取得單一待辦事項", description = "根據ID取得單一待辦事項的詳細資訊")
    public ResponseEntity<MyApiResponse<Todo>> getTodo(@PathVariable Long id) throws
                                                                            TodoNotFoundException {

        logger.info("get todo by id : {}", id);

        Optional<Todo> todo = todos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();

        if (todo.isPresent()) {
            return ResponseEntity.ok(new MyApiResponse<>(true, todo.get(), null));
        } else {
            throw new TodoNotFoundException(id);
//            return createNotFoundError(id);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新待辦事項", description = "根據ID更新一個已存在的待辦事項")
    public ResponseEntity<MyApiResponse<Todo>> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {

        logger.info("update todo, id : {}, todo : {}", id, updatedTodo);

        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getId().equals(id)) {
                updatedTodo.setId(id);
                todos.set(i, updatedTodo);
                return ResponseEntity.ok(new MyApiResponse<>(true, updatedTodo, null));
            }
        }

        return createNotFoundError(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除待辦事項", description = "根據ID刪除一個待辦事項")
    public ResponseEntity<MyApiResponse<Todo>> deleteTodo(@PathVariable Long id) {

        logger.info("delete todo by id : {}", id);

        boolean isSuccess = todos.removeIf(todo -> todo.getId().equals(id));

        if (isSuccess) {
            return ResponseEntity.ok(new MyApiResponse<>(true, null, null));
        }

        return createNotFoundError(id);
    }

    private ResponseEntity<MyApiResponse<Todo>> createNotFoundError(Long id) {
        MyApiResponse.ErrorDetails error = new MyApiResponse.ErrorDetails(
                "https://example.com/errors/not-found",
                "Todo not found",
                HttpStatus.NOT_FOUND,
                MessageFormat.format("Todo with id {0} does not exist", id),
                MessageFormat.format("/api/todos/{0}", id)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MyApiResponse<>(false, null, error));
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