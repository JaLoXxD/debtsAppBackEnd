package com.debts.debtsappbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String secondName;
    private String secondLastName;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String phone;
    @NotNull
    private BigDecimal salary;
}
