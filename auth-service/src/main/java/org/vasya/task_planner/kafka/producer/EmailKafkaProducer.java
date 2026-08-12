package org.vasya.task_planner.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.vasya.task_planner.kafka.contract.EmailSendingEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailKafkaProducer {

    private final KafkaTemplate<String, EmailSendingEvent> kafkaTemplate;

    public void sendTaskToKafka(EmailSendingEvent mail) {

        log.info("send event to kafka");
        kafkaTemplate.send("EMAIL_SENDING_TASKS", mail);
    }
}