package com.debts.debtsappbackend.repository;

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
//    @Query("{ 'debtId' : ?0 }")
    List<DebtPayment> findAllByDebtId(Long debtId);
    Page<DebtPayment> findAllByDebtId(Long debtId, Pageable pageable);
    Optional<DebtPayment> findById(Long debtPaymentId);
    @Modifying
    @Query("UPDATE DebtPayment dp SET dp.name = :name, dp.description = :description, dp.paymentDate = :paymentDate, dp.maxPaymentDate = :maxPaymentDate, dp.amount = :amount, dp.balanceAfterPay = :balanceAfterPay, dp.balanceBeforePay = :balanceBeforePay, dp.image = :image, dp.payed = :payed WHERE dp.id = :debtPaymentId")
    void updateById(Long debtPaymentId, String name, String description, LocalDateTime paymentDate, LocalDateTime maxPaymentDate, BigDecimal amount, BigDecimal balanceAfterPay, BigDecimal balanceBeforePay, String image, Boolean payed);
}
