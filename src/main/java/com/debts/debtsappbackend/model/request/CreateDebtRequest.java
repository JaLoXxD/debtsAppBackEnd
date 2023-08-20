package com.debts.debtsappbackend.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CreateDebtRequest {
    @NotNull
    private String name;
    private String description;
    @NotNull
    private Long category;
    @NotNull
    private Long priority;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    private String collector;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private BigDecimal termInMonths;
}
