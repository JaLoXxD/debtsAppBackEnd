package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.DebtPriorityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateDebtPriorityResponse extends GenericResponse {
    DebtPriorityDto debtPriority;

    public CreateDebtPriorityResponse(Boolean success, String message, String error, DebtPriorityDto debtPriority) {
        super(success, message, error);
        this.debtPriority = debtPriority;
    }
}
