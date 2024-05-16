package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.Debt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    @Transactional
    Debt save(Debt debt);
    @Query("SELECT D FROM Debt D WHERE D.user.id = :userId ORDER BY D.createdAt DESC")
    Page<Debt> findAllByUserId(Long userId, Pageable pageable);
    @Query("SELECT D FROM Debt D WHERE D.user.id = :userId AND (D.name LIKE %:filter% OR D.description LIKE %:filter% OR D.collector LIKE %:filter% OR CAST(D.amount AS string) LIKE %:filter% OR CAST(D.termInMonths AS string) LIKE %:filter%)    ORDER BY D.createdAt DESC")
    Page<Debt> findAllByUserIdAndFilter(Long userId, String filter, Pageable pageable);
    @Modifying
    @Query("UPDATE Debt D SET D.pendingAmount = :pendingAmount WHERE D.id = :debtId")
    void updatePendingAmountById(@Param("debtId") Long debtId, @Param("pendingAmount") BigDecimal pendingAmount);
    Long countAllByUserId(Long userId);
    @Modifying
    @Query("UPDATE Debt D SET D.payed = :payed WHERE D.id = :debtId")
    void updatePayedById(Long debtId, Boolean payed);
}
