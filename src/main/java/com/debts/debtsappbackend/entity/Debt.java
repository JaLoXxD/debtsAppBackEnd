package com.debts.debtsappbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "debt")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "startDate", nullable = false)
    private LocalDateTime startDate;
    @Column(name = "endDate", nullable = false)
    private LocalDateTime endDate;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "collector", nullable = false)
    private String collector;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "pendingAmount", nullable = false)
    private BigDecimal pendingAmount;
    @Column(name = "termInMonths", nullable = false)
    private BigDecimal termInMonths;
    /* RELATIONSHIPS */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "debtCategoryId", nullable = false)
    private DebtCategory debtCategory;
    @ManyToOne
    @JoinColumn(name = "debtPriorityId", nullable = false)
    private DebtPriority debtPriority;
    @OneToMany(mappedBy = "debt")
    private List<DebtPayment> debtPayments = new ArrayList<>();

    public Debt(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Debt{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", createdAt=" + createdAt +
                ", collector='" + collector + '\'' +
                ", amount=" + amount +
                ", pendingAmount=" + pendingAmount +
                ", termInMonths=" + termInMonths +
                '}';
    }
}
