package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    @Transactional
    Debt save(Debt debt);
    List<Debt> findAllByUserId(Long userId);
}
