package com.debts.debtsappbackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DebtPaymentDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime paymentDate;
    private LocalDateTime maxPaymentDate;
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private BigDecimal balanceAfterPay;
    private BigDecimal balanceBeforePay;
    private String image;
    private Boolean payed;
    private DebtDto debt;
}
