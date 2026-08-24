package org.vasya.task_planner.security.authentication.jwt.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.boot.convert.DurationUnit;

import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "jwt-properties")
public record JwtProperties(
       @Valid Config config,
        @Valid Credentials credentials
) {
    public record Config(@NotBlank @Name("token-life-time") String tokenLifeTime) {}
    public record Credentials(@NotBlank @Name("jwt-secret")String jwtSecret) {
        @Override
        @NonNull
        public String toString() {
            return "[PROTECTED]}";
        }
    }
}