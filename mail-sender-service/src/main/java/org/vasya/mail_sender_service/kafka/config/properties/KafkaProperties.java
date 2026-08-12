package org.vasya.mail_sender_service.kafka.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(@Name("bootstrap-servers") String kafkaAddress) {}