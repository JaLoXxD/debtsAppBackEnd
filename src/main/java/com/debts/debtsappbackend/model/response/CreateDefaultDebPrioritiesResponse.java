package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.DebtPriorityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateDefaultDebPrioritiesResponse extends GenericResponse {
    private List<DebtPriorityDto> debtPriorities;
    public CreateDefaultDebPrioritiesResponse(Boolean success, String message, String error, List<DebtPriorityDto> debtPriorities) {
        super(success, message, error);
        this.debtPriorities = debtPriorities;
    }
}
