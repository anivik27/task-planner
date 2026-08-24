package org.vasya.mail_sender_service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.vasya.mail_sender_service.kafka.config.properties.KafkaProperties;

@Configuration
@EnableCaching
@EnableConfigurationProperties(KafkaProperties.class)
public class ApplicationConfig {}