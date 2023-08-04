package com.debts.debtsappbackend.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CreateDebtPriorityRequest {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String color;
}
