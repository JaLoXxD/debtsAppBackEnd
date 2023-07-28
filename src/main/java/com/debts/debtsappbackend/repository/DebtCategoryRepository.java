package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.DebtCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtCategoryRepository extends MongoRepository<DebtCategory, String> {
}
