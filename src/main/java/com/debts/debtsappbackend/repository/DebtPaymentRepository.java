package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.DebtPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DebtPaymentRepository extends MongoRepository<DebtPayment, String> {
    List<DebtPayment> findAllByDebtId(String debtId);
    Optional<DebtPayment> findById(String debtPaymentId);
    @Query("{ '_id' : ?0 }")
    @Update("{ '$set': { 'name': ?1, 'description': ?2, 'paymentDate': ?3, 'maxPaymentDate': ?4, 'amount': ?5, 'balanceAfterPay': ?6, 'balanceBeforePay': ?7, 'image': ?8, 'payed': ?9  } }")
    void updateById(String debtPaymentId, String name, String description, LocalDateTime paymentDate, LocalDateTime maxPaymentDate, BigDecimal amount, BigDecimal balanceAfterPay, BigDecimal balanceBeforePay, String image, Boolean payed);
}
