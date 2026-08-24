package org.vasya.planner.kafka.contract;


public record EmailSendingEvent(
        String email,
        String title,
        String message
) {}