package com.debts.debtsappbackend.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Document(collection = "debt")
public class Debt {
    private String type;
    private String description;
    private String priority;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private String collector;
    private BigDecimal balance;
    /*REFERENCE TO USER DOCUMENT*/
    @DBRef
    private User user;
}
