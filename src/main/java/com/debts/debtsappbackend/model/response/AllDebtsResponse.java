package com.debts.debtsappbackend.model.response;

import com.debts.debtsappbackend.dto.DebtDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllDebtsResponse extends GenericPageResponse {
    private List<DebtDto> debts;
}
