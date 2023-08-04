package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.DebtPaymentDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebtPaymentResponse extends GenericResponse {
    private List<DebtPaymentDto> debtPayment;
}
