package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.DebtPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtPaymentRepository extends MongoRepository<DebtPayment, String> {
    List<DebtPayment> findAllByDebtId(String debtId);
}
