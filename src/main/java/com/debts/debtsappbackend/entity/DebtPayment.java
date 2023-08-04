package com.debts.debtsappbackend.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@Document(collection = "debtPayment")
public class DebtPayment {
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
    /*REFERENCE TO DEBT DOCUMENT*/
    @DBRef
    private Debt debt;
}
