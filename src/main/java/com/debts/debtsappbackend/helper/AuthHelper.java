package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.model.response.JwtResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthHelper {
    public JwtResponse generateLoginResponse(String token){
        return JwtResponse.builder()
                .success(token != null)
                .message(token != null ? "Login successful" : "Login failed")
                .error(token == null ? "Invalid credentials" : null)
                .token(token)
                .build();
    }
}
