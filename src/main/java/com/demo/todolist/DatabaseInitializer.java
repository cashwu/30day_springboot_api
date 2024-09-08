package com.demo.todolist;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final JdbcClient jdbcClient;

    public DatabaseInitializer(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @PostConstruct
    public void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS todos (" +
                "id SERIAL PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "completed BOOLEAN NOT NULL)";

        try {
            jdbcClient.sql(sql).update();
            System.out.println("初始化資料庫成功");
        } catch (Exception e) {
            System.err.println("初始化資料庫失敗：" + e.getMessage());
        }
    }
}
