package com.debts.debtsappbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebtCategoryDto {
    private String id;
    private String name;
    private String description;
    private Boolean global;
    UserDto user;
}
