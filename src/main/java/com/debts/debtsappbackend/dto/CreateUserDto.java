package com.debts.debtsappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String secondName;
    private String secondLastName;
    private String email;
    private String phone;
}
