package com.debts.debtsappbackend.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class DebtPaymentRequest {
    @NotNull
    private String name;
    private String description;
    @NotNull
    private LocalDateTime paymentDate;
    @NotNull
    private LocalDateTime maxPaymentDate;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private BigDecimal balanceAfterPay;
    @NotNull
    private BigDecimal balanceBeforePay;
    private String image;
    @NotNull
    private Boolean payed;
}
