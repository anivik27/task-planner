package org.vasya.task_planner.security.authentication.jwt;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTLogoutHandler implements LogoutHandler {

    private final JWTCookieHelper cookieHelper;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       @Nullable Authentication authentication) {
        cookieHelper.deleteJWT(response);
        response.setStatus(204);
    }
}