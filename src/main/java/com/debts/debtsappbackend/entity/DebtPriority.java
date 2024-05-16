package com.debts.debtsappbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "debtPriority")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DebtPriority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "global", nullable = false)
    private Boolean global;
    @Column(name = "color", nullable = false)
    private String color;
    /* RELATIONSHIPS */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @OneToMany(mappedBy = "debtPriority")
    private List<Debt> debts;

    @Override
    public String toString() {
        return "DebtPriority{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", global=" + global +
                ", color='" + color + '\'' +
                ", user=" + user +
                ", debt=" + debts +
                '}';
    }
}
