package com.demo.todolist.repository;

import com.demo.todolist.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 根據標題查找待辦事項
    @Query("SELECT t FROM Todo t WHERE t.title = :title")
    List<Todo> findByTitle(@Param("title") String title);

    // 查找所有已完成的待辦事項
    @Query("SELECT t FROM Todo t WHERE t.completed = true")
    List<Todo> findByCompletedTrue();

    // 根據標題模糊查詢，並按 ID 降冪序排序
    @Query("SELECT t FROM Todo t WHERE t.title LIKE %:keyword% ORDER BY t.id DESC")
    List<Todo> findByTitleContainingOrderByIdDesc(@Param("keyword") String keyword);

    // 計算未完成的待辦事項數量
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.completed = false")
    long countByCompletedFalse();

    Page<Todo> findAll(Pageable pageable);
}
