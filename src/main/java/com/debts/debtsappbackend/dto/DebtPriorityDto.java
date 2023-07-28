package com.debts.debtsappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DebtPriorityDto {
    private String id;
    private String name;
    private String description;
    private Boolean global;
    private String color;
    private UserDto user;
}
