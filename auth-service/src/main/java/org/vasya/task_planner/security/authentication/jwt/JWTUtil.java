package org.vasya.task_planner.security.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vasya.task_planner.config.properties.ApplicationProperties;
import org.vasya.task_planner.security.service.Role;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTUtil {

    private final ApplicationProperties appProps;


    public String generateToken(Long userId, String email, Role role) {

        Date expirationDate = Date.from(ZonedDateTime.now()
                .plusMinutes(Integer.parseInt(appProps.jwtProperties().config().tokenLifeTime())).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("userId", userId)
                .withClaim("email", email)
                .withClaim("role", role.name())
                .withIssuedAt(new Date())
                .withIssuer("auth")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(appProps.jwtProperties().credentials().jwtSecret()));
    }

    public String validateTokenAndExtractEmail(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(appProps.jwtProperties().credentials().jwtSecret()))
                .withSubject("User details")
                .withIssuer("auth")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}