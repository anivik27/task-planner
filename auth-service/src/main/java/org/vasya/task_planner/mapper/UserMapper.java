package org.vasya.task_planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.vasya.task_planner.dto.auth.UserDTO;
import org.vasya.task_planner.dto.auth.UserResponseDTO;
import org.vasya.task_planner.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDTO toUserDTO(User user);
    UserResponseDTO toUserResponseDTO(UserDTO userDTO);
}