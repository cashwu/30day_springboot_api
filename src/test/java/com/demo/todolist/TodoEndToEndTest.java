package com.demo.todolist;

import com.demo.todolist.model.MyApiResponse;
import com.demo.todolist.model.Todo;
import com.demo.todolist.repository.TodoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TodoEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void createTodo() {

        // 呼叫 Api
        String url = "http://localhost:" + port + "/api/todos";
        Todo newTodo = new Todo(null, "測試待辦事項", false);
        var response = restTemplate.postForEntity(url, newTodo, MyApiResponse.class);

        // 驗證 response 資料
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Todo responseTodo = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});
        assertEquals("測試待辦事項", responseTodo.getTitle());
        assertFalse(responseTodo.isCompleted());

        // 驗證 DB 資料
        List<Todo> todos = todoRepository.findAll();
        assertEquals(1, todos.size());
        assertEquals("測試待辦事項", todos.get(0).getTitle());
        assertFalse(todos.get(0).isCompleted());
    }

    @Test
    void getAllTodos() {

        // 新增測試資料
        Todo todo1 = new Todo(null, "測試待辦事項", false);
        Todo todo2 = new Todo(null, "測試待辦事項2", true);
        todoRepository.saveAll(Arrays.asList(todo1, todo2));

        // 呼叫 Api
        var response = restTemplate.getForEntity("/api/todos", MyApiResponse.class);

        // 驗證 response 資料
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        List<Todo> todos = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertEquals(2, todos.size());
        assertEquals("測試待辦事項", todos.get(0).getTitle());
        assertFalse(todos.get(0).isCompleted());
        assertEquals("測試待辦事項2", todos.get(1).getTitle());
        assertTrue(todos.get(1).isCompleted());
    }

    @Test
    void getTodo() {

        // 新增測試資料
        Todo todo = todoRepository.save(new Todo(null, "測試待辦事項", false));

        // 呼叫 Api
        var response = restTemplate.getForEntity("/api/todos/" + todo.getId(), MyApiResponse.class);

        // 驗證 response 資料
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Todo responseTodo = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});
        assertEquals("測試待辦事項", responseTodo.getTitle());
        assertFalse(responseTodo.isCompleted());
    }

    @Test
    void updateTodo() {

        // 新增測試資料
        Todo todo = todoRepository.save(new Todo(null, "原始待辦事項", false));

        // 呼叫 Api
        Todo updatedTodo = new Todo(todo.getId(), "更新後的待辦事項", true);
        restTemplate.put("/api/todos/" + todo.getId(), updatedTodo);

        // 驗證 DB 資料
        Todo actualTodo = todoRepository.findById(todo.getId()).get();
        assertEquals("更新後的待辦事項", actualTodo.getTitle());
        assertTrue(actualTodo.isCompleted());
    }

    @Test
    void testDeleteTodo() {

        // 新增測試資料
        Todo savedTodo = todoRepository.save(new Todo(null, "要刪除的待辦事項", false));

        // 呼叫 Api
        restTemplate.delete("/api/todos/" + savedTodo.getId());

        // 驗證 DB 資料
        List<Todo> todos = todoRepository.findAll();
        assertEquals(0, todos.size());
    }
}
