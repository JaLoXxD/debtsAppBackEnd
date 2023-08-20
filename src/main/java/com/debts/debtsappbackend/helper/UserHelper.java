package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.dto.UserDto;
import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.model.request.CreateUserRequest;
import com.debts.debtsappbackend.model.response.UserResponse;
import com.debts.debtsappbackend.services.TranslateService;
import com.debts.debtsappbackend.util.StatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class UserHelper {
    private final TranslateService translateService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserHelper(TranslateService translateService, PasswordEncoder passwordEncoder) {
        this.translateService = translateService;
        this.passwordEncoder = passwordEncoder;
    }
    public UserResponse createUserResponse(User user, String labelType, List<String> errors) {
        try {
            if(errors != null)
                return new UserResponse(false, translateService.getMessage("user.error."+ labelType), errors, null);
            return new UserResponse(true, translateService.getMessage("user.success."+ labelType), null, convertToDto(user));
        }catch (Exception e) {
            return new UserResponse(false, translateService.getMessage("user.error."+ labelType), null, null);
        }
    }

    public User setUserDefaultFields(CreateUserRequest createUserRequest){
        User user = mapUserFromRequest(createUserRequest);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(StatusType.ACTIVE.getCode());
        user.setPassword(hashedPassword);
        user.setResetPassword(false);
        return user;
    }

    public UserDto convertToDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .secondName(user.getSecondName())
                .secondLastName(user.getSecondLastName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .phone(user.getPhone())
                .status(user.getStatus())
                .salary(user.getSalary())
                .build();
    }

    public User mapUserFromRequest(CreateUserRequest createUserRequest){
        return User.builder()
                .username(createUserRequest.getUsername())
                .password(createUserRequest.getPassword())
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .secondName(createUserRequest.getSecondName())
                .secondLastName(createUserRequest.getSecondLastName())
                .email(createUserRequest.getEmail())
                .phone(createUserRequest.getPhone())
                .salary(createUserRequest.getSalary())
                .build();
    }
}
