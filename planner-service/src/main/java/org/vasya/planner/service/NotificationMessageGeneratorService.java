package org.vasya.planner.service;

import org.vasya.planner.model.Task;

import java.util.Collection;

public interface NotificationMessageGeneratorService {
    String generateNotification(Collection<Task> tasks);
}