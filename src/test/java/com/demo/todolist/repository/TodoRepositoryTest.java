package com.demo.todolist.repository;

import com.demo.todolist.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author cash.wu
 * @since 2024/09/10
 */
@DataJpaTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void setup() {

        // 在每個測試方法執行前，先清空所有的資料
        todoRepository.deleteAll();

        // 新增測試資料
        todoRepository.saveAll(
                Arrays.asList(new Todo(null, "買牛奶", false), new Todo(null, "寫程式", true),
                              new Todo(null, "看書", false)));
    }

    @Test
    public void findByTitle() {
        List<Todo> todos = todoRepository.findByTitle("買牛奶");
        assertEquals(1, todos.size());
        assertEquals("買牛奶", todos.get(0).getTitle());
    }

    @Test
    public void findByCompletedTrue() {
        List<Todo> completedTodos = todoRepository.findByCompletedTrue();
        assertEquals(1, completedTodos.size());
        assertTrue(completedTodos.get(0).isCompleted());
    }

    @Test
    public void findByTitleContainingOrderByIdDesc() {
        List<Todo> todos = todoRepository.findByTitleContainingOrderByIdDesc("寫");
        assertEquals(1, todos.size());
        assertEquals("寫程式", todos.get(0).getTitle());
    }

    @Test
    public void countByCompletedFalse() {
        long count = todoRepository.countByCompletedFalse();
        assertEquals(2, count);
    }

    @Test
    public void findAllWithPagination() {
        Page<Todo> todoPage = todoRepository.findAll(PageRequest.of(0, 2));
        assertEquals(2, todoPage.getContent().size());
        assertEquals(3, todoPage.getTotalElements());
    }
}
