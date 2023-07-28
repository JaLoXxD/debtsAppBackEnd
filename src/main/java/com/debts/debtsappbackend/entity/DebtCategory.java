package com.debts.debtsappbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "debtCategory")
public class DebtCategory {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotNull
    private String name;
    private String description;
    @NotNull
    private Boolean global;
    /*REFERENCE TO DEBT DOCUMENT*/
    @DBRef
    private Debt debt;
}
