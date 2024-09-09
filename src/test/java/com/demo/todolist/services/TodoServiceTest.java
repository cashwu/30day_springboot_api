package com.demo.todolist.services;

import com.demo.todolist.model.Todo;
import com.demo.todolist.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * @author cash.wu
 * @since 2024/09/09
 */
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

@Test
void testSave() {

    // arrange
    Todo todo = new Todo();
    todo.setTitle("測試待辦事項");
    todo.setCompleted(false);

    when(todoRepository.save(any(Todo.class))).thenReturn(todo);

    // act
    Todo savedTodo = todoService.save(todo);

    // assert
    assertNotNull(savedTodo);
    assertEquals("測試待辦事項", savedTodo.getTitle());
    assertFalse(savedTodo.isCompleted());
    verify(todoRepository, times(1)).save(any(Todo.class));
}

@Test
void testFindById() {

    // arrange
    Long id = 1L;
    Todo todo = new Todo();
    todo.setId(id);
    todo.setTitle("找到的待辦事項");

    when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

    // act
    Optional<Todo> foundTodo = todoService.findById(1L);

    // assert
    assertTrue(foundTodo.isPresent());
    assertEquals("找到的待辦事項", foundTodo.get().getTitle());
    verify(todoRepository, times(1)).findById(id);
}

@Test
void testDeleteTodo() {

    // arrange
    Long id = 1L;

    when(todoRepository.existsById(id)).thenReturn(true);
    doNothing().when(todoRepository).deleteById(id);

    // act
    boolean result = todoService.deleteTodo(1L);

    // assert
    assertTrue(result);
    verify(todoRepository, times(1)).existsById(id);
    verify(todoRepository, times(1)).deleteById(id);
}

    /**
     * 測試在升序排序的情況下獲取分頁結果
     * 模擬了第一頁（page=0），每頁10條記錄，按 id 升序排序的情況
     * 驗證返回的頁面內容、大小和順序是否正確。
     */
    @Test
    void getPagedTodos_ShouldReturnPagedResultsInAscendingOrder() {

        // arrange
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String direction = "asc";

        List<Todo> todos = Arrays.asList(
                new Todo(1L, "Task 1", false),
                new Todo(2L, "Task 2", true),
                new Todo(3L, "Task 3", false)
        );
        Page<Todo> expectedPage = new PageImpl<>(todos);

        when(todoRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // act
        Page<Todo> result = todoService.getPagedTodos(page, size, sortBy, direction);

        // assert
        assertEquals(expectedPage, result);
        assertEquals(3, result.getContent().size());
        assertEquals("Task 1", result.getContent().get(0).getTitle());
        assertEquals("Task 3", result.getContent().get(2).getTitle());
    }


    /**
     * 測試在降序排序的情況下獲取分頁結果
     * 模擬了第二頁（page=1），每頁5條記錄，按 title 降序排序的情況
     * 驗證返回的頁面內容、大小和順序是否正確
     */
    @Test
    void getPagedTodos_ShouldReturnPagedResultsInDescendingOrder() {

        // arrange
        int page = 1;
        int size = 5;
        String sortBy = "title";
        String direction = "desc";

        List<Todo> todos = Arrays.asList(
                new Todo(3L, "Task C", true),
                new Todo(2L, "Task B", false),
                new Todo(1L, "Task A", true)
        );
        Page<Todo> expectedPage = new PageImpl<>(todos);

        when(todoRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // act
        Page<Todo> result = todoService.getPagedTodos(page, size, sortBy, direction);

        // assert
        assertEquals(expectedPage, result);
        assertEquals(3, result.getContent().size());
        assertEquals("Task C", result.getContent().get(0).getTitle());
        assertEquals("Task A", result.getContent().get(2).getTitle());
    }
}
