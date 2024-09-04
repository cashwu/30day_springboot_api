package com.demo.todolist.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author cash.wu
 * @since 2024/09/04
 */
@Component
public class TodoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Todo.class.equals(clazz);
    }

    @Override
    public void validate(Object target,
                         Errors errors) {

        Todo todo = (Todo) target;
        if (todo.getTitle() != null
                && todo.getTitle().contains("urgent")
                && !todo.isCompleted()) {
            errors.rejectValue("completed", "urgent.not.completed", "Urgent todos must be completed");
        }
    }
}
