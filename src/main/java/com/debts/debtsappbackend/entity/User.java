package com.debts.debtsappbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
@Document(collection = "user")
public class User {
    @Id
    private String id;
    @NotNull
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String secondName;
    private String secondLastName;
    @NotNull
    private String email;
    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    @NotNull
    private String phone;
    @NotNull
    private String status;
}
