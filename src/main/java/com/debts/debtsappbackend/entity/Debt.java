package com.debts.debtsappbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "debt")
public class Debt {
    @Id
    private String id;
    @NotNull
    private String category;
    private String description;
    @NotNull
    private String priority;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    private String collector;
    @NotNull
    private BigDecimal balance;
    /*REFERENCE TO USER DOCUMENT*/
    @DBRef
    private User user;
    /*REFERENCE TO DEBT PRIORITY DOCUMENT*/
    @DBRef
    private DebtPriority debtPriority;
}
