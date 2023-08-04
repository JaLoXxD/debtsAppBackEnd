package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends GenericResponse {
    private UserDto user;

    public UserResponse(Boolean success, String message, List<String> errors, UserDto user) {
        super(success, message, errors);
        this.user = user;
    }
}
