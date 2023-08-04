package com.debts.debtsappbackend.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@Builder
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
    /*REFERENCE TO USER DOCUMENT*/
    @DBRef
    private User user;
}
