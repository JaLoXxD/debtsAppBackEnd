package com.debts.debtsappbackend.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Document(collection = "debtPayment")
public class DebtPayment {
    private String name;
    private String description;
    private LocalDate paymentDate;
    private LocalDate maxPaymentDate;
    private LocalDate createdAt;
    private BigDecimal amount;
    private BigDecimal balanceAfterPay;
    private BigDecimal balanceBeforePay;
    private String image;
    private Boolean payed;
    /*REFERENCE TO DEBT DOCUMENT*/
    @DBRef
    private Debt debt;
}
