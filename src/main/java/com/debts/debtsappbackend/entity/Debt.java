package com.debts.debtsappbackend.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@Document(collection = "debt")
public class Debt {
    @Id
    private String id;
    private String category;
    private String name;
    private String description;
    private String priority;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String collector;
    private BigDecimal amount;
    private BigDecimal termInMonths;
    /*REFERENCE TO USER DOCUMENT*/
    @DBRef
    private User user;
}
