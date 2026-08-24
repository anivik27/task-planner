package org.vasya.task_planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vasya.task_planner.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUserId(Long userId);
    Optional<Task> findTaskByIdAndUserId(Long taskId, Long userId);
    void deleteTaskById(Long taskId);
    void deleteTasksByUserId(Long userId);
}