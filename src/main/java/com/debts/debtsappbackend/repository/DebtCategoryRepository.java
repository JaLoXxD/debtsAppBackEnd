package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.DebtCategory;
import com.debts.debtsappbackend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtCategoryRepository extends MongoRepository<DebtCategory, String> {
    void deleteByGlobal(Boolean global);
    DebtCategory findByNameAndGlobal(String name, Boolean global);
    DebtCategory findByNameAndUser(String name, User user);
    List<DebtCategory> findByUserId(String userId);
    List<DebtCategory> findByGlobal(Boolean global);

}
