package org.vasya.task_planner.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.vasya.task_planner.dto.task.TaskRequestDTO;
import org.vasya.task_planner.dto.task.UserDTO;
import org.vasya.task_planner.mapper.TaskMapper;
import org.vasya.task_planner.model.Task;
import org.vasya.task_planner.security.service.UserDetailsImpl;
import org.vasya.task_planner.service.TaskService;

import java.util.List;

@RestController
@RequestMapping(value = "/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getTasks(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<UserDTO> tasks = taskService.getAllTasks(userDetails.getId());
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tasks);
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws ValidationException {

        Task task = taskMapper.toTask(taskRequestDTO);
        UserDTO userDTO = taskService.addNewTaskToUser(userDetails.getId(), task);
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTO);
    }


    @PostMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateTask(@PathVariable(name = "id") Long taskId,
                                              @Valid @RequestBody TaskRequestDTO taskRequestDTO,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) throws ValidationException {

        Task task = taskMapper.toTask(taskRequestDTO);
        task.setId(taskId);
        UserDTO userDTO = taskService.updateTask(userDetails.getId(), task);
        return ResponseEntity
                .status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTO);
    }


    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteTask(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) throws ValidationException {

        taskService.deleteTask(userDetails.getId(), id);
        return ResponseEntity
                .status(204)
                .build();
    }


    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteTasks(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ValidationException {

        taskService.deleteTasks(userDetails.getId());
        return ResponseEntity
                .status(204)
                .build();
    }
}