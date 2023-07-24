package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.model.DebtCreatedModel;
import com.debts.debtsappbackend.entity.Debt;
import org.springframework.stereotype.Component;

@Component
public class DebtHelper {
    public DebtCreatedModel createDebtResponse(Debt debt) {
        try {
            return new DebtCreatedModel(true, "Debt created successfully", "", debt);
        } catch (Exception e) {
            return new DebtCreatedModel(false, "Error creating debt", e.getMessage(), null);
        }
    }
}
