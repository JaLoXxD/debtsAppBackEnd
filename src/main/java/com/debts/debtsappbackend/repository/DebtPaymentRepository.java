package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.entity.DebtPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DebtPaymentRepository extends JpaRepository<DebtPayment, Long> {
    Page<DebtPayment> findAllByDebtId(Long debtId, Pageable pageable);
    @Query("SELECT D FROM DebtPayment D WHERE D.debt.id = :debtId AND (D.name LIKE %:filter% OR D.description LIKE %:filter% OR CAST(D.amount AS string) LIKE %:filter% OR CAST(D.balanceAfterPay AS string) LIKE %:filter% OR CAST(D.balanceBeforePay AS string) LIKE %:filter% OR FUNCTION('DATE_FORMAT', D.paymentDate, '%Y-%m-%d %T') LIKE %:filter% OR FUNCTION('DATE_FORMAT', D.maxPaymentDate, '%Y-%m-%d %T') LIKE %:filter%) ORDER BY D.createdAt DESC")
    Page<DebtPayment> findAllByDebtIdAndFilter(Long debtId, String filter, Pageable pageable);
    Optional<DebtPayment> findFirstByDebtIdAndPayedOrderByCreatedAtDesc(Long debtId, Boolean payed);
    Optional<DebtPayment> findById(Long debtPaymentId);
    void deleteAllByDebtId(Long debtId);
    void deleteAllByDebtIdAndPayed(Long debtId, Boolean payed);
    List<DebtPayment> findAllByDebtIdAndPayed(Long debtId, Boolean payed);
    Long countAllByDebtId(Long debtId);
    @Modifying
    @Query("UPDATE DebtPayment dp SET dp.name = :name, dp.description = :description, dp.paymentDate = :paymentDate, dp.amount = :amount, dp.balanceAfterPay = :balanceAfterPay, dp.balanceBeforePay = :balanceBeforePay, dp.image = :image, dp.payed = :payed WHERE dp.id = :debtPaymentId")
    void updateById(Long debtPaymentId, String name, String description, LocalDateTime paymentDate, BigDecimal amount, BigDecimal balanceAfterPay, BigDecimal balanceBeforePay, String image, Boolean payed);
    @Modifying
    @Query("UPDATE DebtPayment dp SET dp.expectedAmount = :expectedAmount WHERE dp.debt.id = :debtId AND dp.payed = false")
    void updateExpectedAmountByDebtId(Long debtId, BigDecimal expectedAmount);
    @Modifying
    @Query("UPDATE DebtPayment dp SET dp.expectedAmount = :expectedAmount WHERE dp.id = :debtPaymentId AND dp.payed = false")
    void updateExpectedAmountByDebtPaymentId(Long debtPaymentId, BigDecimal expectedAmount);
}
