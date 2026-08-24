package org.vasya.task_planner.security.authentication.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.vasya.task_planner.dto.auth.UserDTO;
import org.vasya.task_planner.security.authentication.jwt.JWTCookieHelper;
import org.vasya.task_planner.security.authentication.jwt.JWTUtil;
import org.vasya.task_planner.security.service.UserDetailsImpl;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final JWTCookieHelper cookieHelper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {

        log.info("successful authentication");
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserDTO user = userDetails.getUserDTO();

        String jwt = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        cookieHelper.setJWT(jwt, response);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}