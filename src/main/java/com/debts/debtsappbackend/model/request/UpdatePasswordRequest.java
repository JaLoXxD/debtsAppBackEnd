package com.debts.debtsappbackend.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class UpdatePasswordRequest {
    @NotNull
    private String currentPassword;
    @NotNull
    private String newPassword;
    @NotNull
    private String confirmPassword;
}
