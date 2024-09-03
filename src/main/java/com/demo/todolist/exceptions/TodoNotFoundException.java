package com.demo.todolist.exceptions;

/**
 * @author cash.wu
 * @since 2024/09/03
 */
public class TodoNotFoundException extends Exception {

    public TodoNotFoundException(Long id) {
        super("Todo not found with id: " + id);
    }
}
