package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.dto.DebtPaymentDto;
import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.entity.DebtPayment;
import com.debts.debtsappbackend.model.response.DebtPaymentResponse;
import com.debts.debtsappbackend.services.TranslateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DebtPaymentHelper extends GenericHelper{
    private final TranslateService translateService;

    public DebtPaymentHelper(TranslateService translateService) {
        super(translateService);
        this.translateService = translateService;
    }

    public DebtPayment mapGenericDebtPayment(String name, LocalDateTime paymentDate, BigDecimal amount, Debt debt){
        return DebtPayment.builder()
                .name(name)
                .paymentDate(paymentDate)
                .maxPaymentDate(paymentDate)
                .createdAt(LocalDateTime.now())
                .amount(BigDecimal.valueOf(0))
                .expectedAmount(amount)
                .payed(false)
                .debt(debt)
                .build();
    }

    public DebtPaymentDto convertDebtPaymentToDto(DebtPayment debtPayment){
        return DebtPaymentDto.builder()
                .id(debtPayment.getId())
                .name(debtPayment.getName())
                .description(debtPayment.getDescription())
                .paymentDate(debtPayment.getPaymentDate())
                .maxPaymentDate(debtPayment.getMaxPaymentDate())
                .createdAt(debtPayment.getCreatedAt())
                .amount(debtPayment.getAmount())
                .expectedAmount(debtPayment.getExpectedAmount())
                .balanceAfterPay(debtPayment.getBalanceAfterPay())
                .balanceBeforePay(debtPayment.getBalanceBeforePay())
                .image(debtPayment.getImage())
                .payed(debtPayment.getPayed())
                .build();
    }
}
