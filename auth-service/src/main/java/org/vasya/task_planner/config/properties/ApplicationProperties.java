package org.vasya.task_planner.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.vasya.task_planner.security.authentication.jwt.properties.JwtProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(@Name("jwt-properties") JwtProperties jwtProperties) {}