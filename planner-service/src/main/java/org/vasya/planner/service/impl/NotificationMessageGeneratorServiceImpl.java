package org.vasya.planner.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vasya.planner.model.Task;
import org.vasya.planner.service.NotificationMessageGeneratorService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationMessageGeneratorServiceImpl implements NotificationMessageGeneratorService {

    private static final int TASK_LIMIT = 5;
    private static final String DEFAULT = "You have empty list of tasks";
    private static final String COMPLETED = "You have %d completed tasks:\n%s";
    private static final String UNCOMPLETED = "You have %d completed tasks:\n%s";
    private static final String COMPLETED_AND_UNCOMPLETED = "You have %d completed tasks:\n%s and %d\n uncompleted tasks:\n%s";

    public String generateNotification(Collection<Task> tasks) {

        log.info("generations notification");

        Map<Boolean, List<Task>> grouped = tasks.stream()
                .collect(Collectors.partitioningBy(Task::isStatus));
        int completedSize = grouped.get(true).size();
        int uncompletedSize = grouped.get(false).size();
        List<Task> completed = grouped.get(true).stream().limit(TASK_LIMIT).toList();
        List<Task> uncompleted = grouped.get(false).stream().limit(TASK_LIMIT).toList();

        if (!completed.isEmpty() && !uncompleted.isEmpty()) {
            return COMPLETED_AND_UNCOMPLETED.formatted(
                    completedSize,
                    completed.stream()
                            .map(Task::getTitle)
                            .collect(Collectors.joining("\n")),
                    uncompletedSize,
                    uncompleted.stream()
                            .map(Task::getTitle)
                            .collect(Collectors.joining("\n"))
            );
        }

        if (!completed.isEmpty()) {
            return COMPLETED.formatted(
                    completedSize,
                    completed.stream()
                            .map(Task::getTitle)
                            .collect(Collectors.joining("\n"))
            );
        }

        if (!uncompleted.isEmpty()) {
            return UNCOMPLETED.formatted(
                    uncompletedSize,
                    uncompleted.stream()
                            .map(Task::getTitle)
                            .collect(Collectors.joining("\n"))
            );
        }

        return DEFAULT;
    }
}