package org.vasya.task_planner.mapper;

import org.mapstruct.*;
import org.vasya.task_planner.dto.task.TaskRequestDTO;
import org.vasya.task_planner.dto.task.UserDTO;
import org.vasya.task_planner.model.Task;
import org.vasya.task_planner.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task toTask(TaskRequestDTO taskRequestDTO);

    @Mapping(target = "userId", source = "user", qualifiedByName = "userToUserId")
    UserDTO toTaskResponseDTO(Task task);

    @Mapping(target = "userId", source = "user", qualifiedByName = "userToUserId")
    List<UserDTO> toTaskListResponseDTO(List<Task> task);

    @Named("userToUserId")
    default Long convertLong(User user) {
        return user.getId();
    }
}