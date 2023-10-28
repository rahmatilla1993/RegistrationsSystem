package com.example.accountingsystem.security;

import com.example.accountingsystem.payload.AccessDeniedResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException
    ) throws IOException {
        var accessDeniedResponse = new AccessDeniedResponse();
        accessDeniedResponse.setPath(request.getRequestURL().toString());
        String json = new Gson().toJson(accessDeniedResponse);
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().println(json);
        log.error(accessDeniedException.getMessage());
    }
}
