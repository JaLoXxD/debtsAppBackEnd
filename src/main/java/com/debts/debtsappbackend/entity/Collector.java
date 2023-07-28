package com.debts.debtsappbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Document(collection = "collector")
public class Collector {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal totalDebt;
    /*REFERENCE TO DEBT DOCUMENT*/
    @DBRef
    private Debt debt;
}
