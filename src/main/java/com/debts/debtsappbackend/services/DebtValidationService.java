package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.repository.DebtRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DebtValidationService {
    private final DebtRepository debtRepository;
    private final TranslateService translateService;

    public DebtValidationService(DebtRepository debtRepository, TranslateService translateService) {
        this.debtRepository = debtRepository;
        this.translateService = translateService;
    }

    public Debt checkIfDebtExists(Long debtId, List<String> errors){
        Debt debt = debtRepository.findById(debtId).orElse(null);
        if(debt == null){
            errors.add(translateService.getMessage("debt.not.found"));
        }
        return debt;
    }
}
