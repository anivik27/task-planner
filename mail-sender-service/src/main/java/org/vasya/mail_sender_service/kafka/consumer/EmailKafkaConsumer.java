package org.vasya.mail_sender_service.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.vasya.mail_sender_service.kafka.contract.EmailSendingEvent;
import org.vasya.mail_sender_service.kafka.exceprion.NonRetryableException;
import org.vasya.mail_sender_service.kafka.exceprion.RetryableException;
import org.vasya.mail_sender_service.service.NotificationService;


@Service
@KafkaListener(topics = "EMAIL_SENDING_TASKS",
        groupId = "mail_sender_group",
        containerFactory = "kafkaListenerContainerFactory")
@Slf4j
@RequiredArgsConstructor
public class EmailKafkaConsumer {

    private final NotificationService notificationServiceImpl;

    @KafkaHandler
    public void handle(EmailSendingEvent emailSendingEvent, Acknowledgment ack) {
        try {
            log.info("got kafka event" + emailSendingEvent);
            notificationServiceImpl.sendRegistrationMessage(
                    emailSendingEvent.email(),
                    emailSendingEvent.title(),
                    emailSendingEvent.message()
            );
            ack.acknowledge();
        } catch (MailSendException e) {
            log.info("retryable exception for" + emailSendingEvent);
            throw new RetryableException(e);
        } catch (Exception e) {
            log.info("non retryable exception for" + emailSendingEvent);
            throw new NonRetryableException(e);
        }
    }
}