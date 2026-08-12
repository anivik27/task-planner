package org.vasya.task_planner.service;

import org.vasya.task_planner.dto.task.UserDTO;
import org.vasya.task_planner.model.Task;

import java.util.List;

public interface TaskService {
    List<UserDTO> getAllTasks(Long userId);
    UserDTO addNewTaskToUser(Long userId, Task task);
    UserDTO updateTask(Long userId, Task task);
    void deleteTask(Long userId, Long taskId);
    void deleteTasks(Long userId);
}