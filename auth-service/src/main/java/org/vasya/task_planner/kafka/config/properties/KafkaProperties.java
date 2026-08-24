package org.vasya.task_planner.kafka.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(@Name("bootstrap-servers") String kafkaAddress) {}