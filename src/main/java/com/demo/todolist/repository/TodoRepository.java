package com.demo.todolist.repository;

import com.demo.todolist.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 根據標題查找待辦事項
    List<Todo> findByTitle(String title);

    // 查找所有已完成的待辦事項
    List<Todo> findByCompletedTrue();

    // 根據標題模糊查詢，並按 ID 降冪序排序
    List<Todo> findByTitleContainingOrderByIdDesc(String keyword);

    // 計算未完成的待辦事項數量
    long countByCompletedFalse();
}
