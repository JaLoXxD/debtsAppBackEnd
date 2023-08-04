package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.Debt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends MongoRepository<Debt, String> {
    Debt save(Debt debt);
    List<Debt> findAllByUserId(String userId);
}
