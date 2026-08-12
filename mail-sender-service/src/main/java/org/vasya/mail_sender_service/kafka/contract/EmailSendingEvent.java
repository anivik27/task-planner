package org.vasya.mail_sender_service.kafka.contract;

public record EmailSendingEvent(
        String email,
        String title,
        String message
) {}