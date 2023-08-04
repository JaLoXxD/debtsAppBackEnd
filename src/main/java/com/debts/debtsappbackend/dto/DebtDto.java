package com.debts.debtsappbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebtDto {
    private String id;
    private String name;
    private String description;
    private DebtCategoryDto category;
    private DebtPriorityDto priority;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String collector;
    private BigDecimal amount;
    private UserDto user;
    private List<DebtPaymentDto> debtPayments;
}
