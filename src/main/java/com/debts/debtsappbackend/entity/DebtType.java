package com.debts.debtsappbackend.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "debtType")
public class DebtType {
    private String name;
    private String description;
    private Boolean global;
    /*REFERENCE TO DEBT DOCUMENT*/
    @DBRef
    private Debt debt;
}
