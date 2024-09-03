package com.demo.todolist.controller;

import com.demo.todolist.exceptions.TodoNotFoundException;
import com.demo.todolist.model.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author cash.wu
 * @since 2024/09/03
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTodoNotFoundException(TodoNotFoundException e) {
        ApiResponse.ErrorDetails error = new ApiResponse.ErrorDetails(
                "https://example.com/errors/not-found",
                "Todo not found",
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                apiPath()
        );
        return ResponseEntity.status(404).body(new ApiResponse<>(false, null, error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {

        String apiPath = apiPath();
        ApiResponse.ErrorDetails error = new ApiResponse.ErrorDetails(
                "https://example.com/errors/internal-error",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                apiPath
        );
        return ResponseEntity.status(500).body(new ApiResponse<>(false, null, error));
    }

    private static String apiPath() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getRequestURI();
    }
}