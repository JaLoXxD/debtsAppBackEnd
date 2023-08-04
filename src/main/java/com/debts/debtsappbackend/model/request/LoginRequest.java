package com.debts.debtsappbackend.model.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
