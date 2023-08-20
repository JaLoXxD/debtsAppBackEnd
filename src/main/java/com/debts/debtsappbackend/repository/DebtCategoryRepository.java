package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.DebtCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtCategoryRepository extends JpaRepository<DebtCategory, Long> {
    void deleteByGlobal(Boolean global);
    DebtCategory findByNameAndGlobal(String name, Boolean global);
    DebtCategory findByNameAndUserId(String name, Long userId);
    Optional<DebtCategory> findById(Long id);
    List<DebtCategory> findByUserId(Long userId);
    List<DebtCategory> findByGlobal(Boolean global);

}
