package org.vasya.task_planner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vasya.task_planner.dto.task.UserDTO;
import org.vasya.task_planner.mapper.TaskMapper;
import org.vasya.task_planner.model.Task;
import org.vasya.task_planner.model.User;
import org.vasya.task_planner.repository.TaskRepository;
import org.vasya.task_planner.repository.UserRepository;
import org.vasya.task_planner.service.TaskService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    @Transactional(readOnly = true)
    public List<UserDTO> getAllTasks(Long userId) {
        log.info("getting all tasks");
        return taskMapper.toTaskListResponseDTO(taskRepository.findAllByUserId(userId));
    }


    @Transactional
    public UserDTO addNewTaskToUser(Long userId, Task task) {

        log.info("add new task" + task.getId());
        User user = userRepository.getReferenceById(userId);
        user.addTask(task);
        return taskMapper.toTaskResponseDTO(taskRepository.saveAndFlush(task));
    }


    @Transactional
    public UserDTO updateTask(Long userId, Task task) {

        log.info("update task" + task.getId());
        Optional<Task> optionalTaskInDB = taskRepository.findTaskByIdAndUserId(task.getId(), userId);
        if (optionalTaskInDB.isEmpty()) {
            throw new ObjectNotFoundException(UserDTO.class, "task not found");
        }

        Task updatedTask = optionalTaskInDB.get();
        updatedTask.setStatus(task.isStatus());
        updatedTask.setTitle(task.getTitle());
        updatedTask.setDescription(task.getDescription());
        taskRepository.saveAndFlush(updatedTask);
        return taskMapper.toTaskResponseDTO(updatedTask);
    }


    @Transactional
    public void deleteTask(Long userId, Long taskId) {

        log.info("delete task" + taskId);
        Optional<Task> optionalTaskInDB = taskRepository.findTaskByIdAndUserId(taskId, userId);
        if (optionalTaskInDB.isEmpty()) {
            throw new ObjectNotFoundException(Task.class, "task not found");
        }

        taskRepository.deleteTaskById(taskId);
    }


    @Transactional
    public void deleteTasks(Long userId) {
        log.info("delete all tasks");
        taskRepository.deleteTasksByUserId(userId);
    }
}