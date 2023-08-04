package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.DebtCategoryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateDebtCategoryResponse extends GenericResponse{
    private DebtCategoryDto debtCategory;
}
