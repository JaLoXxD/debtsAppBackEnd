package com.debts.debtsappbackend.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "debtPayment")
public class DebtPayment {
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
    /*REFERENCE TO DEBT DOCUMENT*/
    @DBRef
    private Debt debt;
}
