package org.vasya.task_planner.kafka.contract;


public record EmailSendingEvent(
        String email,
        String title,
        String message
) {}