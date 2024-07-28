package com.back.studio.configuration;

import com.back.studio.auth.responses.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            objectMapper.writeValue(response.getWriter(), new ApiResponse(
                    new Date(),
                    HttpStatus.OK,
                    "Successfully logged out",
                    request.getRequestURI()
            ));
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
