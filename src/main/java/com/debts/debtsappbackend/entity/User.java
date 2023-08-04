package com.debts.debtsappbackend.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String secondName;
    private String secondLastName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    private String phone;
    private String status;
    private BigDecimal salary;
    private Boolean resetPassword;

    public User(String username, String password, String firstName, String lastName, String secondName, String secondLastName, String email, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin, String phone, String status) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
        this.secondLastName = secondLastName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLogin = lastLogin;
        this.phone = phone;
        this.status = status;
    }
}
