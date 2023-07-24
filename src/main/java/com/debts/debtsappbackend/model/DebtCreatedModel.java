package com.debts.debtsappbackend.model;

import com.debts.debtsappbackend.entity.Debt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DebtCreatedModel extends GenericResponseModel {
    private Debt debt;

    public DebtCreatedModel(Boolean success, String message, String error, Debt debt) {
        super(success, message, error);
        this.debt = debt;
    }
}
