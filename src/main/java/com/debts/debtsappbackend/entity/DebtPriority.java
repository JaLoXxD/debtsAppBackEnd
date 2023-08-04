package com.debts.debtsappbackend.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@Builder
@Document(collection = "debtPriority")
public class DebtPriority {
    @Id
    private String id;
    private String name;
    private String description;
    private Boolean global;
    private String color;
    /*REFERENCE TO USER DOCUMENT*/
    @DBRef
    private User user;
}
