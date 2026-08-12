package org.vasya.mail_sender_service.service;

public interface NotificationService {
    void sendRegistrationMessage(String to, String subject, String body);
}