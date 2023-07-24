package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.Debt;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DebtRepository extends MongoRepository<Debt, String> {
    Debt save(Debt debt);
}
