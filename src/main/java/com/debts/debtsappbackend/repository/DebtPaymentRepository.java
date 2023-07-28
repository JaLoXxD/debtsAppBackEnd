package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.DebtPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtPaymentRepository extends MongoRepository<DebtPayment, String> {

}
