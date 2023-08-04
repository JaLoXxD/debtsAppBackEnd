package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.DebtDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebtResponse extends GenericResponse {
    private DebtDto debt;
}
