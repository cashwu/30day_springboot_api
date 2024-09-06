package com.demo.todolist;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS todos (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "completed BOOLEAN NOT NULL)";

        try {
            jdbcTemplate.execute(sql);
            System.out.println("初始化資料庫成功");
        } catch (Exception e) {
            System.err.println("初始化資料庫失敗：" + e.getMessage());
        }
    }
}
