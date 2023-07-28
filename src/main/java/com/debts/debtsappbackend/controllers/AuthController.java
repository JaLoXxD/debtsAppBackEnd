package com.debts.debtsappbackend.controllers;

import com.debts.debtsappbackend.model.response.JwtResponse;
import com.debts.debtsappbackend.model.request.LoginRequest;
import com.debts.debtsappbackend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws  Exception{
        log.info("ENTER LOGIN REST");
        JwtResponse jwtResponse = userService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
