package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.DebtPriority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DebtPriorityRepository extends MongoRepository<DebtPriority, String> {
    DebtPriority save(DebtPriority debtPriority);
    DebtPriority findByName(String name);
    @Query("{ '_id' : ?0 }")
    void updateDebtPriority(String id, String name, String description, Boolean global, String color);
    DebtPriority findByNameAndGlobal(String name, Boolean global);
    void deleteByGlobal(Boolean global);
    List<DebtPriority> findByUserId(String userId);
    List<DebtPriority> findByGlobal(Boolean global);
}