package com.demo.todolist.controller;

import com.demo.todolist.exceptions.TodoNotFoundException;
import com.demo.todolist.model.MyApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author cash.wu
 * @since 2024/09/03
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<MyApiResponse<Void>> handleTodoNotFoundException(TodoNotFoundException e) {

        logger.error("todo not found exception occurred", e);

        MyApiResponse.ErrorDetails error = new MyApiResponse.ErrorDetails(
                "https://example.com/errors/not-found",
                "Todo not found",
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                apiPath()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MyApiResponse<>(false, null, error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyApiResponse<Void>> handleException(Exception e) {

        logger.error("Unhandled exception occurred", e);

        String apiPath = apiPath();
        MyApiResponse.ErrorDetails error = new MyApiResponse.ErrorDetails(
                "https://example.com/errors/internal-error",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                apiPath
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MyApiResponse<>(false, null, error));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        logger.error("http message not readable occurred", ex);

        MyApiResponse.ErrorDetails error = new MyApiResponse.ErrorDetails(
                "https://example.com/errors/invalid-json",
                "無法解析請求內容",
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MyApiResponse<>(false, null, error));
    }

    private static String apiPath() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getRequestURI();
    }
}