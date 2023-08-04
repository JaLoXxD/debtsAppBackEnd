package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.DebtPriorityDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultDebtPrioritiesResponse extends GenericResponse {
    private List<DebtPriorityDto> debtPriorities;
}
