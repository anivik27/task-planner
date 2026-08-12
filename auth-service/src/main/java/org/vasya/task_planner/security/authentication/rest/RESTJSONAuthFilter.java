package org.vasya.task_planner.security.authentication.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.vasya.task_planner.dto.auth.UserLoginRequestDTO;
import org.vasya.task_planner.security.authentication.handler.AuthSuccessHandler;

import java.io.IOException;
import java.util.Map;


@Component
@Slf4j
public class RESTJSONAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Autowired
    public RESTJSONAuthFilter(AuthenticationManager authenticationManager,
                              AuthSuccessHandler handler,
                              ObjectMapper objectMapper) {
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/auth/login");
        setAuthenticationSuccessHandler(handler);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (request.getContentType() == null || !request.getContentType().contains("application/json")) {
            throw new AuthenticationServiceException("Content-Type must be application/json");
        }
        try {
            @Valid UserLoginRequestDTO loginResuest = objectMapper.readValue(request.getInputStream(), UserLoginRequestDTO.class);
            UsernamePasswordAuthenticationToken token =
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            loginResuest.email(),
                            loginResuest.password());
            setDetails(request, token);

            log.info("authentication");
            return getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Invalid JSON", e);
        }
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        log.info("unsuccessful authentication");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> body = Map.of("message", failed.getMessage());
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}