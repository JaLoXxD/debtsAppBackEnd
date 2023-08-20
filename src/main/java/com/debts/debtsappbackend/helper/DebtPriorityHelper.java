package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.dto.DebtPriorityDto;
import com.debts.debtsappbackend.dto.UserDto;
import com.debts.debtsappbackend.entity.DebtPriority;
import com.debts.debtsappbackend.model.request.CreateDebtPriorityRequest;
import com.debts.debtsappbackend.model.response.CreateDebtPriorityResponse;
import com.debts.debtsappbackend.model.response.DefaultDebtPrioritiesResponse;
import com.debts.debtsappbackend.model.response.GetAllDebtsPrioritiesResponse;
import com.debts.debtsappbackend.services.TranslateService;
import com.debts.debtsappbackend.util.DebtPriorityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DebtPriorityHelper {
    private final TranslateService translateService;
    private final UserHelper userHelper;

    public DebtPriorityHelper(TranslateService translateService, UserHelper userHelper){
        this.translateService = translateService;
        this.userHelper = userHelper;
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
                    .debtPriority(convertDebtPriorityToDto(debtPriority, true))
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

    public DebtPriority mapDebtPriorityFromRequest(CreateDebtPriorityRequest request){
        return DebtPriority.builder()
                .name(request.getName().toUpperCase())
                .description(request.getDescription())
                .global(false)
                .color(request.getColor())
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

    public DebtPriorityDto convertDebtPriorityToDto(DebtPriority debtPriority, Boolean withUser){
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
                    .debtPriorities(debtPriorities.stream().map(debtPriority -> convertDebtPriorityToDto(debtPriority, false)).collect(Collectors.toList()))
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

    public GetAllDebtsPrioritiesResponse buildGetAllDebtsPrioritiesResponse(List<DebtPriority> userDebtPriorities, List<DebtPriority> globalDebtPriorities, List<String> errors){
        try{
            if(!errors.isEmpty())
                return GetAllDebtsPrioritiesResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.priority.get.all.error"))
                        .errors(errors)
                        .build();
            List<DebtPriorityDto> userDebtPrioritiesDto = userDebtPriorities.stream().map(debtPriority -> convertDebtPriorityToDto(debtPriority, false)).collect(Collectors.toList());
            List<DebtPriorityDto> globalDebtPrioritiesDto = globalDebtPriorities.stream().map(debtPriority -> convertDebtPriorityToDto(debtPriority, false)).collect(Collectors.toList());
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
}
