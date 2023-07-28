package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.DebtPriorityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllDebsPrioritiesResponse extends GenericResponse {
    List<DebtPriorityDto> allDebtPriorities;
    List<DebtPriorityDto> globalDebtPriorities;
    List<DebtPriorityDto> userDebtPriorities;

    public GetAllDebsPrioritiesResponse(Boolean success, String message, String error, List<DebtPriorityDto> allDebtPriorities, List<DebtPriorityDto> globalDebtPriorities, List<DebtPriorityDto> userDebtPriorities) {
        super(success, message, error);
        this.allDebtPriorities = allDebtPriorities;
        this.globalDebtPriorities = globalDebtPriorities;
        this.userDebtPriorities = userDebtPriorities;
    }
}
