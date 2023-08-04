package com.debts.debtsappbackend.model.request;

import com.debts.debtsappbackend.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
public class CreateDebtCategoryRequest {
    @NotNull
    private String name;
    private String description;
    UserDto user;
}
