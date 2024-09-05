package com.demo.todolist;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    @PostConstruct
    public void initDatabase() {

        String url = "jdbc:h2:~/test";
        String user = "sa";
        String password = "";

        String sql = "CREATE TABLE IF NOT EXISTS todos (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "completed BOOLEAN NOT NULL)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("初始化資料庫建成功");

        } catch (Exception e) {
            System.err.println("初始化資料庫失敗：" + e.getMessage());
        }
    }
}
