package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.dto.*;
import com.debts.debtsappbackend.entity.*;
import com.debts.debtsappbackend.model.request.CreateDebtCategoryRequest;
import com.debts.debtsappbackend.model.request.CreateDebtPriorityRequest;
import com.debts.debtsappbackend.model.request.CreateDebtRequest;
import com.debts.debtsappbackend.model.response.*;
import com.debts.debtsappbackend.services.TranslateService;
import com.debts.debtsappbackend.util.DebtCategoryType;
import com.debts.debtsappbackend.util.DebtPriorityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DebtHelper extends GenericHelper{
    private final TranslateService translateService;
    private final UserHelper userHelper;

    @Autowired
    public DebtHelper(TranslateService translateService, UserHelper userHelper){
        this.translateService = translateService;
        this.userHelper = userHelper;
    }

    public DebtResponse buildDebtResponse(Debt debt, List<DebtPayment> debtPayments, List<String> errors) {
        try{
            if(!errors.isEmpty()){
                return DebtResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.create.error"))
                        .errors(errors)
                        .build();
            }
            return DebtResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.create.success"))
                    .debt(convertDebtToDto(debt, debtPayments, true))
                    .build();
        } catch(Exception e) {
            log.info("ERROR: " + e);
            return DebtResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.create.error"))
                    .errors(errors)
                    .build();
        }
    }

    public AllDebtsResponse buildAllDebtsResponse(List<Debt> debts, List<String> errors) {
        try{
            log.error("ERRORS: {}", errors);
            if(!errors.isEmpty()){
                return AllDebtsResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.get.all.error"))
                        .errors(errors)
                        .build();
            }
            return AllDebtsResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.get.all.success"))
                    .debts(debts.stream().map(debt -> convertDebtToDto(debt, null, false)).collect(Collectors.toList()))
                    .build();
        } catch(Exception e){
            return AllDebtsResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.get.all.error"))
                    .errors(errors)
                    .build();
        }
    }

    public DebtPaymentResponse buildDebtPaymentResponse(DebtPayment debtPayment, List<String> errors){
        try{
            if(!errors.isEmpty()){
                return DebtPaymentResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.payment.update.error"))
                        .errors(errors)
                        .build();
            }
            return DebtPaymentResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.payment.update.success"))
                    .debtPayment(convertDebtPaymentToDto(debtPayment))
                    .build();
        } catch(Exception e) {
            return DebtPaymentResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.payment.update.error"))
                    .errors(errors)
                    .build();
        }
    }

    public DefaultDebtPrioritiesResponse buildDefaultDebtPrioritiesResponse(List<DebtPriority> debtPriorities, List<String> errors) {
        try {
            if(!errors.isEmpty())
                return DefaultDebtPrioritiesResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.create.defaultPriorities.error"))
                        .errors(errors)
                        .build();
            return DefaultDebtPrioritiesResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.create.defaultPriorities.success"))
                    .debtPriorities(debtPriorities.stream().map(debtPriority -> _convertDebtPriorityToDto(debtPriority, false)).collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            log.info("ERROR: ", e);
            return DefaultDebtPrioritiesResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.create.defaultPriorities.error"))
                    .errors(errors)
                    .build();
        }
    }

    public DefaultDebtCategoriesResponse buildDefaultDebtCategoriesResponse(List<DebtCategory> debtCategories, List<String> errors) {
        try{
           if(!errors.isEmpty()){
                return DefaultDebtCategoriesResponse.builder()
                          .success(false)
                          .message(translateService.getMessage("debt.create.defaultCategories.error"))
                          .errors(errors)
                          .build();
           }
           return DefaultDebtCategoriesResponse.builder()
                   .success(true)
                   .message(translateService.getMessage("debt.create.defaultCategories.success"))
                   .debtCategories(debtCategories.stream().map(debtCategory -> _convertDebtCategoryToDto(debtCategory, false)).collect(Collectors.toList()))
                   .build();
        } catch(Exception e){
            return DefaultDebtCategoriesResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.create.defaultCategories.error"))
                    .errors(errors)
                    .build();
        }
    }

    public GetAllDebtsPrioritiesResponse buildGetAllDebtsPrioritiesResponse(List<DebtPriority> userDebtPriorities, List<DebtPriority> globalDebtPriorities, List<String> errors){
        try{
            if(!errors.isEmpty())
                return GetAllDebtsPrioritiesResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.priority.get.all.error"))
                        .errors(errors)
                        .build();
            List<DebtPriorityDto> userDebtPrioritiesDto = userDebtPriorities.stream().map(debtPriority -> _convertDebtPriorityToDto(debtPriority, false)).collect(Collectors.toList());
            List<DebtPriorityDto> globalDebtPrioritiesDto = globalDebtPriorities.stream().map(debtPriority -> _convertDebtPriorityToDto(debtPriority, false)).collect(Collectors.toList());
            List<DebtPriorityDto> allDebtPrioritiesDto = new ArrayList<>();
            allDebtPrioritiesDto.addAll(userDebtPrioritiesDto);
            allDebtPrioritiesDto.addAll(globalDebtPrioritiesDto);
            return GetAllDebtsPrioritiesResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.priority.get.all.success"))
                    .allDebtPriorities(allDebtPrioritiesDto)
                    .userDebtPriorities(userDebtPrioritiesDto)
                    .globalDebtPriorities(globalDebtPrioritiesDto)
                    .build();
        } catch(Exception e){
            log.info("ERROR: ", e);
            return GetAllDebtsPrioritiesResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.priority.get.all.error"))
                    .errors(errors)
                    .build();
        }
    }

    public GetAllDebtsCategoriesResponse buildGetAllDebtsCategoriesResponse(List<DebtCategory> userDebtCategories, List<DebtCategory> globalDebtCategories, List<String> errors) {
        try{
            if(!errors.isEmpty()){
                return GetAllDebtsCategoriesResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.category.get.all.error"))
                        .errors(errors)
                        .build();
            }
            List<DebtCategoryDto> userDebtCategoriesDto = userDebtCategories.stream().map(debtCategory -> _convertDebtCategoryToDto(debtCategory, false)).collect(Collectors.toList());
            List<DebtCategoryDto> globalDebtCategoriesDto = globalDebtCategories.stream().map(debtCategory -> _convertDebtCategoryToDto(debtCategory, false)).collect(Collectors.toList());
            List<DebtCategoryDto> allDebtCategoriesDto = new ArrayList<>();
            allDebtCategoriesDto.addAll(userDebtCategoriesDto);
            allDebtCategoriesDto.addAll(globalDebtCategoriesDto);
            return GetAllDebtsCategoriesResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.category.get.all.success"))
                    .userDebtCategories(userDebtCategoriesDto)
                    .globalDebtCategories(globalDebtCategoriesDto)
                    .allDebtCategories(allDebtCategoriesDto)
                    .build();
        } catch(Exception e) {
            return GetAllDebtsCategoriesResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.category.get.all.error"))
                    .errors(errors)
                    .build();
        }
    }

    public CreateDebtPriorityResponse buildCreateDebtPriorityResponse(DebtPriority debtPriority, List<String> errors){
        try{
            if(!errors.isEmpty())
                return CreateDebtPriorityResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.priority.create.error"))
                        .errors(errors)
                        .build();
            return CreateDebtPriorityResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.priority.create.success"))
                    .debtPriority(_convertDebtPriorityToDto(debtPriority, true))
                    .build();
        } catch(Exception e){
            log.info("ERROR: ", e);
            return CreateDebtPriorityResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.priority.create.error"))
                    .errors(errors)
                    .build();
        }
    }

    public CreateDebtCategoryResponse buildCreateDebtCategoryResponse(DebtCategory debtCategory, List<String> errors){
        try {
            if(!errors.isEmpty()){
                return CreateDebtCategoryResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.category.create.error"))
                        .errors(errors)
                        .build();
            }
            return CreateDebtCategoryResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.category.create.success"))
                    .debtCategory(_convertDebtCategoryToDto(debtCategory, true))
                    .build();
        } catch(Exception e){
            return CreateDebtCategoryResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.category.create.error"))
                    .errors(errors)
                    .build();
        }
    }

    private DebtPriorityDto _convertDebtPriorityToDto(DebtPriority debtPriority, Boolean withUser){
        UserDto userDto = debtPriority.getUser() != null ? userHelper.convertToDto(debtPriority.getUser()) : null;
        return DebtPriorityDto.builder()
                .id(debtPriority.getId())
                .name(debtPriority.getName())
                .description(translateService.getMessage(debtPriority.getDescription()))
                .global(debtPriority.getGlobal())
                .color(debtPriority.getColor())
                .user(withUser ? userDto : null)
                .build();
    }

    private DebtCategoryDto _convertDebtCategoryToDto(DebtCategory debtCategory, Boolean withUser){
        UserDto userDto = debtCategory.getUser() != null ? userHelper.convertToDto(debtCategory.getUser()) : null;
        return DebtCategoryDto.builder()
                .id(debtCategory.getId())
                .name(debtCategory.getName())
                .description(translateService.getMessage(debtCategory.getDescription()))
                .global(debtCategory.getGlobal())
                .user(withUser ? userDto : null)
                .build();
    }

    public DebtPriority convertDebtPriorityTypeToDebtPriority(DebtPriorityType debtPriorityType){
        return DebtPriority.builder()
                .name(debtPriorityType.getCode())
                .description(debtPriorityType.getDescription())
                .global(true)
                .color(debtPriorityType.getColor())
                .build();
    }

    public DebtCategory convertDebtCategoryTypeToDebtCategory(DebtCategoryType debtCategoryType){
        return DebtCategory.builder()
                .name(debtCategoryType.getCode())
                .description(debtCategoryType.getDescription())
                .global(true)
                .build();
    }

    public DebtDto convertDebtToDto(Debt debt, List<DebtPayment> debtPayments, Boolean withUser){
        List<DebtPaymentDto> debtPaymentsDto = new ArrayList<>();
        if(debtPayments != null){
            debtPaymentsDto = debtPayments.stream().map(this::convertDebtPaymentToDto).collect(Collectors.toList());
        }

        return  DebtDto.builder()
                .id(debt.getId())
                .priority(_convertDebtPriorityToDto(debt.getPriority(), false))
                .category(_convertDebtCategoryToDto(debt.getCategory(), false))
                .name(debt.getName())
                .description(debt.getDescription())
                .startDate(debt.getStartDate())
                .endDate(debt.getEndDate())
                .collector(debt.getCollector())
                .amount(debt.getAmount())
                .user(withUser ? userHelper.convertToDto(debt.getUser()) : null)
                .debtPayments(debtPaymentsDto.isEmpty() ? null : debtPaymentsDto)
                .build();
    }

    public DebtPaymentDto convertDebtPaymentToDto(DebtPayment debtPayment){
        return DebtPaymentDto.builder()
                .name(debtPayment.getName())
                .description(debtPayment.getDescription())
                .paymentDate(debtPayment.getPaymentDate())
                .maxPaymentDate(debtPayment.getMaxPaymentDate())
                .createdAt(debtPayment.getCreatedAt())
                .amount(debtPayment.getAmount())
                .balanceAfterPay(debtPayment.getBalanceAfterPay())
                .balanceBeforePay(debtPayment.getBalanceBeforePay())
                .image(debtPayment.getImage())
                .payed(debtPayment.getPayed())
                .build();
    }

    public Debt mapDebtFromRequest(CreateDebtRequest request, User user, DebtCategory debtCategory, DebtPriority debtPriority){
        return Debt.builder()
                .category(debtCategory)
                .name(request.getName())
                .description(request.getDescription())
                .priority(debtPriority)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .collector(request.getCollector())
                .amount(request.getAmount())
                .termInMonths(request.getTermInMonths())
                .user(user)
                .build();
    }

    public DebtPriority mapDebtPriorityFromRequest(CreateDebtPriorityRequest request){
        return DebtPriority.builder()
                .name(request.getName().toUpperCase())
                .description(request.getDescription())
                .global(false)
                .color(request.getColor())
                .build();
    }

    public DebtCategory mapDebtCategoryFromRequest(CreateDebtCategoryRequest request){
        return DebtCategory.builder()
                .name(request.getName().toUpperCase())
                .description(request.getDescription())
                .global(false)
                .build();
    }

    public DebtPayment mapGenericDebtPayment(String name, LocalDateTime paymentDate, BigDecimal amount, Debt debt){
        return DebtPayment.builder()
                .name(name)
                .paymentDate(paymentDate)
                .maxPaymentDate(paymentDate)
                .createdAt(LocalDateTime.now())
                .amount(amount)
                .payed(false)
                .debt(debt)
                .build();
    }
}
