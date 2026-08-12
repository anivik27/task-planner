package org.vasya.task_planner.security.authentication.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vasya.task_planner.config.properties.ApplicationProperties;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTCookieHelper {

    private final ApplicationProperties appProps;

    public void setJWT(String jwt, HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt-auth", jwt);
        jwtCookie.setMaxAge(Integer.parseInt(appProps.jwtProperties().config().tokenLifeTime()));
        jwtCookie.setDomain(appProps.domain());
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setAttribute("SameSite", "Lax");

        response.addCookie(jwtCookie);
    }

    public Optional<String> extractJWT(Cookie[] cookies) {

        if(cookies == null || cookies.length == 0) {
            return Optional.empty();
        }

        return Arrays.stream(cookies).
                filter(c -> "jwt-auth".equals(c.getName()))
                .map(Cookie::getValue)
                .filter(Objects::nonNull)
                .findFirst();
    }

    public void deleteJWT(HttpServletResponse response) {
        Cookie clearedCookie = new Cookie("jwt-auth", "");
        clearedCookie.setHttpOnly(true);
        clearedCookie.setPath("/");
        clearedCookie.setMaxAge(0);
        clearedCookie.setSecure(false);

        response.addCookie(clearedCookie);
    }
}