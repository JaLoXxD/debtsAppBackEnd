package com.debts.debtsappbackend.model;

import com.debts.debtsappbackend.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreatedModel extends GenericResponseModel{
    private UserDto user;

    public UserCreatedModel(Boolean success, String message, String error, UserDto user) {
        super(success, message, error);
        this.user = user;
    }
}
