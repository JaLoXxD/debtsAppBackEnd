package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.dto.UserDto;
import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.model.request.CreateUserRequest;
import com.debts.debtsappbackend.model.response.CreateUserResponse;
import com.debts.debtsappbackend.services.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UserHelper {
    private final TranslateService translateService;

    @Autowired
    public UserHelper(TranslateService translateService){
        this.translateService = translateService;
    }
    public CreateUserResponse createUserResponse(User user, String error, Locale locale) {
        try {
            if(error != null)
                return new CreateUserResponse(false, translateService.getMessage("user.error.create", locale), translateService.getMessage(error, locale), null);
            return new CreateUserResponse(true, translateService.getMessage("user.success.create", locale), null, convertToDto(user));
        }catch (Exception e) {
            return new CreateUserResponse(false, translateService.getMessage("user.error.create", locale), e.getMessage(), null);
        }
    }

    public UserDto convertToDto(User user){
        return new UserDto(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getSecondName(), user.getSecondLastName(), user.getCreatedAt(), user.getUpdatedAt(), user.getLastLogin(), user.getPhone(), user.getStatus());
    }

    public User mapUserFromRequest(CreateUserRequest userRequest){
        return User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .secondName(userRequest.getSecondName())
                .secondLastName(userRequest.getSecondLastName())
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .build();
    }
}
