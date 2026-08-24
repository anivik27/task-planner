package org.vasya.planner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vasya.planner.kafka.contract.EmailSendingEvent;
import org.vasya.planner.kafka.producer.EmailKafkaProducer;
import org.vasya.planner.model.User;
import org.vasya.planner.repository.TaskRepository;
import org.vasya.planner.repository.UserRepository;
import org.vasya.planner.service.UserTaskService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTaskServiceImpl implements UserTaskService {

    private static final int PAGE_SIZE = 100;

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EmailKafkaProducer kafkaTaskProducer;
    private final NotificationMessageGeneratorServiceImpl notificationMessageGenerator;

    public void processAllUsers() {

        log.info("keyset pagination");

        Long lastId = 0L;

        while (true) {

            List<Long> usersIds = taskRepository.test(lastId, PAGE_SIZE);


            if (usersIds.isEmpty()) {
                break;
            }

            List<User> usersWithTasks = userRepository.findUsersByIdIn(usersIds);

            for (User user : usersWithTasks) {
                process(user);
            }

            lastId = usersIds.getLast();
        }
    }

    private void process(User user) {
        log.info(String.format("building event contract \nemail: %s\nnotification: %s",
                user.getEmail(), notificationMessageGenerator.generateNotification(user.getTasks())));
        kafkaTaskProducer.sendTaskToKafka(
                new EmailSendingEvent(
                        user.getEmail(),
                        "Summary for the day",
                        notificationMessageGenerator.generateNotification(user.getTasks())
                )
        );
    }
}