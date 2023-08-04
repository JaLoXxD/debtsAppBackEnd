package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.DebtCategory;
import com.debts.debtsappbackend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtCategoryRepository extends MongoRepository<DebtCategory, String> {
    void deleteByGlobal(Boolean global);
    DebtCategory findByNameAndGlobal(String name, Boolean global);
    DebtCategory findByNameAndUser(String name, User user);
    Optional<DebtCategory> findByIdAndUserId(String id, String userId);
    List<DebtCategory> findByUserId(String userId);
    List<DebtCategory> findByGlobal(Boolean global);

}
