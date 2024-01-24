package com.debts.debtsappbackend.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class DebtPaymentRequestParams {
    private Long debtId;
    private String name;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private BigDecimal pendingAmount;
    private Boolean payed;

    // Getters and setters...
}
