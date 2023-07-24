package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebtService {
    private final DebtRepository repository;

    @Autowired
    public DebtService(DebtRepository repository){
        this.repository = repository;
    }

    public Debt createDebt(Debt debt) {
        return repository.save(debt);
    }
}
