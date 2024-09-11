package com.demo.todolist;

import com.demo.todolist.model.Todo;
import com.demo.todolist.repository.TodoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TodoEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        todoRepository.deleteAll();
    }

    @Test
    void createTodo() {

        Todo newTodo = new Todo(null, "測試待辦事項", false);

        // 呼叫 API
        given().contentType(ContentType.JSON)
               .body(newTodo)
               .when()
               .post("/api/todos")
               .then()
               .statusCode(200)
               .body("success", equalTo(true))
               .body("data.title", equalTo("測試待辦事項"))
               .body("data.completed", equalTo(false));

        // 驗證資料庫
        assertThat(todoRepository.findAll()).hasSize(1);
    }

    @Test
    void getAllTodos() {

        // 新增測試資料
        Todo todo1 = new Todo(null, "測試待辦事項", false);
        Todo todo2 = new Todo(null, "測試待辦事項2", true);
        todoRepository.saveAll(Arrays.asList(todo1, todo2));

        // 呼叫 API
        given()
                .when()
                .get("/api/todos")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data", hasSize(2))
                .body("data[0].title", equalTo("測試待辦事項"))
                .body("data[0].completed", equalTo(false))
                .body("data[1].title", equalTo("測試待辦事項2"))
                .body("data[1].completed", equalTo(true));
    }

    @Test
    void getTodo() {

        // 新增測試資料
        Todo todo = todoRepository.save(new Todo(null, "測試待辦事項", false));

        // 呼叫 API
        given()
                .when()
                .get("/api/todos/{id}", todo.getId())
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.title", equalTo("測試待辦事項"))
                .body("data.completed", equalTo(false));
    }

    @Test
    void updateTodo() {

        // 新增測試資料
        Todo todo = todoRepository.save(new Todo(null, "原始待辦事項", false));

        // 呼叫 API
        Todo updatedTodo = new Todo(todo.getId(), "更新後的待辦事項", true);

        given()
                .contentType(ContentType.JSON)
                .body(updatedTodo)
                .when()
                .put("/api/todos/{id}", todo.getId())
                .then()
                .statusCode(200);

        // 驗證資料庫
        Todo actualTodo = todoRepository.findById(todo.getId()).orElseThrow();
        assertThat(actualTodo.getTitle()).isEqualTo("更新後的待辦事項");
        assertThat(actualTodo.isCompleted()).isTrue();
    }

    @Test
    void testDeleteTodo() {

        // 新增測試資料
        Todo savedTodo = todoRepository.save(new Todo(null, "要刪除的待辦事項", false));

        // 呼叫 API
        given()
                .when()
                .delete("/api/todos/{id}", savedTodo.getId())
                .then()
                .statusCode(200);

        // 驗證資料庫
        assertThat(todoRepository.findAll()).isEmpty();
    }
}
