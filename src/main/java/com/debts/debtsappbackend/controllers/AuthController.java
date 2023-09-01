package com.debts.debtsappbackend.controllers;

import com.debts.debtsappbackend.model.request.CreateUserRequest;
import com.debts.debtsappbackend.model.request.LoginRequest;
import com.debts.debtsappbackend.model.request.RecoverPasswordRequest;
import com.debts.debtsappbackend.model.response.JwtResponse;
import com.debts.debtsappbackend.model.response.RecoverPasswordResponse;
import com.debts.debtsappbackend.model.response.UserResponse;
import com.debts.debtsappbackend.services.UserService;
import com.debts.debtsappbackend.services.ValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    private final UserService userService;
    private final ValidatorService validatorService;
    public AuthController(UserService userService, ValidatorService validatorService) {
        this.userService = userService;
        this.validatorService = validatorService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody CreateUserRequest user, HttpServletRequest request, BindingResult bindingResult) {
        try {
            log.info("ENTER USER REGISTER REST {}", request.getRequestURI());
            validatorService.validate("user", user, bindingResult);
            if(bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.registerUser(null, validatorService.getErrors(bindingResult)));
            }
            UserResponse response = userService.registerUser(user, null);
            HttpStatus status = response.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userService.registerUser(null, List.of(e.getMessage())));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest, BindingResult bindingResult){
        try{
            log.info("ENTER LOGIN REST");
            validatorService.validate("user", loginRequest, bindingResult);
            if(bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.login(null, validatorService.getErrors(bindingResult)));
            }
            JwtResponse response = userService.login(loginRequest, new ArrayList<>());
            HttpStatus status = response.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userService.login(loginRequest, List.of(e.getMessage())));
        }
    }

    @PostMapping("/recover-password")
    public ResponseEntity<RecoverPasswordResponse> recoverPassword(@RequestBody RecoverPasswordRequest recoverPasswordRequest, BindingResult bindingResult){
        try {
            log.info("ENTER RECOVER PASSWORD REST");
            validatorService.validate("user", recoverPasswordRequest, bindingResult);
            if(bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.recoverPassword(null, validatorService.getErrors(bindingResult)));
            }
            RecoverPasswordResponse response = userService.recoverPassword(recoverPasswordRequest, new ArrayList<>());
            HttpStatus status = response.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userService.recoverPassword(null, List.of(e.getMessage())));
        }
    }
}
