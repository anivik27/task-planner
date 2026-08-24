package org.vasya.task_planner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vasya.task_planner.dto.auth.UserDTO;
import org.vasya.task_planner.exception.UserAlreadyExistException;
import org.vasya.task_planner.mapper.UserMapper;
import org.vasya.task_planner.model.User;
import org.vasya.task_planner.repository.UserRepository;
import org.vasya.task_planner.security.service.Role;
import org.vasya.task_planner.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserDTO create(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(Role.USER);

        try {
            log.info("create user");
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            log.info("user already exist");
            throw new UserAlreadyExistException(String.format("user with %s already exist", email));
        }

        return userMapper.toUserDTO(user);
    }
}