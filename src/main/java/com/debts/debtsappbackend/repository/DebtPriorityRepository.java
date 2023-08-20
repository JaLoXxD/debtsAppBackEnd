package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.DebtPriority;
import com.debts.debtsappbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtPriorityRepository extends JpaRepository<DebtPriority, Long> {
    DebtPriority findByName(String name);
//    @Query("{ '_id' : ?0 }")
//    void updateDebtPriority(String id, String name, String description, Boolean global, String color);
    DebtPriority findByNameAndGlobal(String name, Boolean global);
    DebtPriority findByNameAndUser(String name, User user);
    Optional<DebtPriority> findById(Long id);
    void deleteByGlobal(Boolean global);
    List<DebtPriority> findByUserId(Long userId);
    List<DebtPriority> findByGlobal(Boolean global);
}
