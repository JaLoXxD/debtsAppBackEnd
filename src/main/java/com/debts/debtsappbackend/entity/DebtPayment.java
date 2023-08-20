package com.debts.debtsappbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "debtPayment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//TODO: ADD TO STRING METHOD
public class DebtPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "paymentDate", nullable = false)
    private LocalDateTime paymentDate;
    @Column(name = "maxPaymentDate", nullable = false)
    private LocalDateTime maxPaymentDate;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "balanceAfterPay")
    private BigDecimal balanceAfterPay;
    @Column(name = "balanceBeforePay")
    private BigDecimal balanceBeforePay;
    @Column(name = "image")
    private String image;
    @Column(name = "payed", nullable = false)
    private Boolean payed;
    /* RELATIONSHIPS */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debtId", nullable = false)
    private Debt debt;

    @Override
    public String toString() {
        return "DebtPayment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", paymentDate=" + paymentDate +
                ", maxPaymentDate=" + maxPaymentDate +
                ", createdAt=" + createdAt +
                ", amount=" + amount +
                ", balanceAfterPay=" + balanceAfterPay +
                ", balanceBeforePay=" + balanceBeforePay +
                ", image='" + image + '\'' +
                ", payed=" + payed +
                ", debt=" + debt +
                '}';
    }
}
