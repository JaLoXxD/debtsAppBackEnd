package com.debts.debtsappbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//TODO: ADD TO STRING METHOD
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private Long id;
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;
    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;
    @Column(name = "secondName", length = 50)
    private String secondName;
    @Column(name = "secondLastName", length = 50)
    private String secondLastName;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
    @Column(name = "lastLogin")
    private LocalDateTime lastLogin;
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    @Column(name = "salary", nullable = false)
    private BigDecimal salary;
    @Column(name = "resetPassword", nullable = false)
    private Boolean resetPassword;
    /* RELATIONSHIPS */
    @OneToMany(mappedBy = "user")
    private List<Debt> debts;
    @OneToMany(mappedBy = "user")
    private List<DebtCategory> debtCategories;
    @OneToMany(mappedBy = "user")
    private List<DebtPriority> debtPriorities;
}
