package org.vasya.task_planner.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.vasya.task_planner.dto.auth.UserDTO;
import org.vasya.task_planner.dto.auth.UserRegistrationRequestDTO;
import org.vasya.task_planner.dto.auth.UserResponseDTO;
import org.vasya.task_planner.kafka.contract.EmailSendingEvent;
import org.vasya.task_planner.kafka.producer.EmailKafkaProducer;
import org.vasya.task_planner.mapper.UserMapper;
import org.vasya.task_planner.security.service.UserDetailsImpl;
import org.vasya.task_planner.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserMapper userMapper;
    private final AuthService authServiceImpl;
    private final EmailKafkaProducer kafkaTaskProducer;

    @PostMapping(value = "/registration",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> registration(@Valid @RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO,
                                                            HttpServletResponse response) throws ValidationException {

        UserDTO savedUser = authServiceImpl.registration(userRegistrationRequestDTO, response);
        EmailSendingEvent kafkaEvent = new EmailSendingEvent(
                savedUser.getEmail(),
                "registration",
                "welcome to task planner");
        kafkaTaskProducer.sendTaskToKafka(kafkaEvent);
        return ResponseEntity.status(201).build();
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ValidationException {

        UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(userDetails.getUserDTO());
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userResponseDTO);
    }
}