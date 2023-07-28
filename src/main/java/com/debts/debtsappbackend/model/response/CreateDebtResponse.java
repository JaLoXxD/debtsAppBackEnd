package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.entity.Debt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateDebtResponse extends GenericResponse {
    private Debt debt;

    public CreateDebtResponse(Boolean success, String message, String error, Debt debt) {
        super(success, message, error);
        this.debt = debt;
    }
}
