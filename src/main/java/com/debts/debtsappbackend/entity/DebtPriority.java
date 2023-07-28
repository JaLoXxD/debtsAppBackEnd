package com.debts.debtsappbackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Document(collection = "debtPriority")
public class DebtPriority {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean global;
    @NotNull
    private String color;
    /*REFERENCE TO USER DOCUMENT*/
    @DBRef
    private User user;

    public DebtPriority(String name, String description, Boolean global, String color, User user) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.color = color;
        this.user = user;
    }
}
