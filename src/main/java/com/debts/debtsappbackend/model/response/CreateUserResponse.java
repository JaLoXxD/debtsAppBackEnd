package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserResponse extends GenericResponse {
    private UserDto user;

    public CreateUserResponse(Boolean success, String message, String error, UserDto user) {
        super(success, message, error);
        this.user = user;
    }
}
