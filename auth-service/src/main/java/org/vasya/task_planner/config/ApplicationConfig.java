package org.vasya.task_planner.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.vasya.task_planner.config.properties.ApplicationProperties;
import org.vasya.task_planner.kafka.config.properties.KafkaProperties;
import org.vasya.task_planner.security.authentication.jwt.properties.JwtProperties;

@Configuration
@EnableCaching
@EnableConfigurationProperties({JwtProperties.class, ApplicationProperties.class, KafkaProperties.class})
public class ApplicationConfig {}