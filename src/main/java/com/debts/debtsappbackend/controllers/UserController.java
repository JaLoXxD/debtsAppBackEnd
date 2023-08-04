package com.debts.debtsappbackend.controllers;

import com.debts.debtsappbackend.model.request.UpdatePasswordRequest;
import com.debts.debtsappbackend.model.request.CreateUserRequest;
import com.debts.debtsappbackend.model.request.UpdateUserRequest;
import com.debts.debtsappbackend.model.response.GenericResponse;
import com.debts.debtsappbackend.model.response.UserResponse;
import com.debts.debtsappbackend.services.UserService;
import com.debts.debtsappbackend.services.ValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final ValidatorService validatorService;

    @Autowired
    public UserController(UserService userService, ValidatorService validatorService) {
        this.userService = userService;
        this.validatorService = validatorService;
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserData(@RequestHeader("Authorization") String token){
        try{
            log.info("ENTER REST GET USER DATA");
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserData(token));
        } catch (Exception e){
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userService.getUserData(null));
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<GenericResponse> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest, @RequestHeader("Authorization") String token, BindingResult bindingResult){
        try{
            log.info("ENTER REST UPDATE PASSWORD");
            validatorService.validate("user", updatePasswordRequest, bindingResult);
            if(bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.updatePassword(null, token, validatorService.getErrors(bindingResult)));
            }
            return ResponseEntity.status(HttpStatus.OK).body(userService.updatePassword(updatePasswordRequest, token, new ArrayList<>()));
        } catch (Exception e){
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userService.updatePassword(null, token, List.of(e.getMessage())));
        }
    }

    @PutMapping("/update-user")
    public ResponseEntity<GenericResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest, @RequestHeader("Authorization") String token, BindingResult bindingResult){
        try{
            log.info("ENTER REST UPDATE USER");
            validatorService.validate("user", updateUserRequest, bindingResult);
            if(bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.updateUser(null, token, validatorService.getErrors(bindingResult)));
            }
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(updateUserRequest, token, new ArrayList<>()));
        } catch (Exception e){
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userService.updateUser(null, token, List.of(e.getMessage())));
        }
    }
}
