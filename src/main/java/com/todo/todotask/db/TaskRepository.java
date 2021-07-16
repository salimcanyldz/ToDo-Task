package com.todo.todotask.db;

import com.todo.todotask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("select t from Task t where t.id = :id")
    public Task findTaskByID(@Param("id") int id);
}
