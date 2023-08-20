package com.debts.debtsappbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "debtCategory")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//TODO: ADD TO STRING METHOD
public class DebtCategory {
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
    /* RELATIONSHIPS */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @OneToMany(mappedBy = "debtCategory")
    private List<Debt> debts;

    @Override
    public String toString() {
        return "DebtCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", global=" + global +
                ", user=" + user +
                ", debt=" + debts +
                '}';
    }
}
