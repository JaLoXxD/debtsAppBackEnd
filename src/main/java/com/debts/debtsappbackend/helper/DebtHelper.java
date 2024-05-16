package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.dto.*;
import com.debts.debtsappbackend.entity.*;
import com.debts.debtsappbackend.model.request.CreateDebtRequest;
import com.debts.debtsappbackend.model.response.*;
import com.debts.debtsappbackend.services.TranslateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DebtHelper extends GenericHelper{
    private final TranslateService translateService;
    private final UserHelper userHelper;

    private final DebtPaymentHelper debtPaymentHelper;
    private final DebtCategoryHelper debtCategoryHelper;
    private final DebtPriorityHelper debtPriorityHelper;

    public DebtHelper(TranslateService translateService, UserHelper userHelper, DebtPaymentHelper debtPaymentHelper, DebtCategoryHelper debtCategoryHelper, DebtPriorityHelper debtPriorityHelper) {
        super(translateService);
        this.translateService = translateService;
        this.userHelper = userHelper;
        this.debtPaymentHelper = debtPaymentHelper;
        this.debtCategoryHelper = debtCategoryHelper;
        this.debtPriorityHelper = debtPriorityHelper;
    }

    public DebtResponse buildDebtResponse(Debt debt, List<DebtPayment> debtPayments, Boolean create, List<String> errors) {
        try{
            if(!errors.isEmpty()){
                return DebtResponse.builder()
                        .success(false)
                        .message(translateService.getMessage(create ? "debt.create.error" : "debt.info.error"))
                        .errors(errors)
                        .build();
            }
            return DebtResponse.builder()
                    .success(true)
                    .message(translateService.getMessage(create ? "debt.create.success" : "debt.info.success"))
                    .debt(convertDebtToDto(debt, debtPayments, true))
                    .build();
        } catch(Exception e) {
            log.info("ERROR: " + e);
            return DebtResponse.builder()
                    .success(false)
                    .message(translateService.getMessage(create ? "debt.create.error" : "debt.info.error"))
                    .errors(errors)
                    .build();
        }
    }

    public AllDebtsResponse buildAllDebtsResponse(List<Debt> debts, Long totalPages, Long totalElements, List<String> errors) {
        if(!errors.isEmpty()){
            return AllDebtsResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.get.all.error"))
                    .errors(errors)
                    .totalPages(totalPages)
                    .totalElements(totalElements)
                    .build();
        }
        return AllDebtsResponse.builder()
                .success(true)
                .message(translateService.getMessage("debt.get.all.success"))
                .debts(debts.stream().map(debt -> convertDebtToDto(debt, null, false)).collect(Collectors.toList()))
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    public DebtPaymentResponse buildDebtPaymentResponse(Debt debt, List<DebtPayment> debtPayments, Long totalElements, Long totalPages, List<String> errors){
        try{
            if(!errors.isEmpty()){
                return DebtPaymentResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.payment.get.error"))
                        .errors(errors)
                        .build();
            }
            return DebtPaymentResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.payment.get.success"))
                    .debtPayments(debtPayments != null ? debtPayments.stream().map(this.debtPaymentHelper::convertDebtPaymentToDto).collect(Collectors.toList()) : null)
                    .debt(convertDebtToDto(debt, null, false))
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .build();
        } catch(Exception e) {
            return DebtPaymentResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.payment.get.error"))
                    .errors(errors)
                    .build();
        }
    }

    public DebtDto convertDebtToDto(Debt debt, List<DebtPayment> debtPayments, Boolean withUser){
        return  DebtDto.builder()
                .id(debt.getId())
                .priority(debtPriorityHelper.convertDebtPriorityToDto(debt.getDebtPriority(), false))
                .category(debtCategoryHelper.convertDebtCategoryToDto(debt.getDebtCategory(), false))
                .name(debt.getName())
                .description(debt.getDescription())
                .startDate(debt.getStartDate())
                .endDate(debt.getEndDate())
                .collector(debt.getCollector())
                .amount(debt.getAmount())
                .pendingAmount(debt.getPendingAmount())
                .termInMonths(debt.getTermInMonths())
                .payed(debt.getPayed())
                .user(withUser ? userHelper.convertToDto(debt.getUser()) : null)
                .debtPayments(debtPayments != null ? debtPayments.stream().map(debtPaymentHelper::convertDebtPaymentToDto).collect(Collectors.toList()) : null)
                .build();
    }

    public Debt mapDebtFromRequest(CreateDebtRequest request, User user, DebtCategory debtCategory, DebtPriority debtPriority){
        log.info("ENTER MAP DEBT FROM REQUEST");
        return Debt.builder()
                .debtCategory(debtCategory)
                .name(request.getName())
                .description(request.getDescription())
                .debtPriority(debtPriority)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdAt(LocalDateTime.now())
                .collector(request.getCollector())
                .amount(request.getAmount())
                .pendingAmount(request.getAmount())
                .termInMonths(request.getTermInMonths())
                .payed(false)
                .debtPayments(new ArrayList<>())
                .user(user)
                .build();
    }
}
