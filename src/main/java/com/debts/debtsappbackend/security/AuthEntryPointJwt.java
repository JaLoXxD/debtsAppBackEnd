package com.debts.debtsappbackend.security;

import com.debts.debtsappbackend.model.response.GenericResponse;
import com.debts.debtsappbackend.services.TranslateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();
    private final TranslateService translateService;

    public AuthEntryPointJwt(TranslateService translateService) {
        this.translateService = translateService;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {}", authException.getMessage());
        GenericResponse genericResponse = GenericResponse.builder()
                .success(false)
                .message(translateService.getMessage("general.error.unauthorized.description"))
                .errors(List.of(translateService.getMessage("general.error.unauthorized")))
                .build();
        String json = mapper.writeValueAsString(genericResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }
}