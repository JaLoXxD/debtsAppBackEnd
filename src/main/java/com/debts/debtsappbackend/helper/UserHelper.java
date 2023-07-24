package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.dto.UserDto;
import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.model.UserCreatedModel;
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
    public UserCreatedModel createUserResponse(User user, String error, Locale locale) {
        try {
            if(error != null)
                return new UserCreatedModel(false, translateService.getMessage("user.error.create", locale), translateService.getMessage(error, locale), null);
            return new UserCreatedModel(true, "User created successfully", null, _convertToDto(user));
        }catch (Exception e) {
            return new UserCreatedModel(false, translateService.getMessage("user.error.create", locale), e.getMessage(), null);
        }
    }

    private UserDto _convertToDto(User user){
        return new UserDto(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getSecondName(), user.getSecondLastName(), user.getCreatedAt(), user.getUpdatedAt(), user.getLastLogin(), user.getPhone(), user.getStatus());
    }
}
