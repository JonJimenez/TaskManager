package com.hcl.TaskManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.TaskManager.model.Task;

@Repository
public interface TaskRepository extends	JpaRepository<Task, Long> {
    List<Task> findAll();
    Task findById(Integer taskId);
    //@Query("SELECT t FROM Task t WHERE t.userId=:userId")
    //Task findById(@Param("userId") Integer userId);
    void delete(Task task);
}