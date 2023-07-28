package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.dto.DebtPriorityDto;
import com.debts.debtsappbackend.dto.UserDto;
import com.debts.debtsappbackend.entity.DebtPriority;
import com.debts.debtsappbackend.model.response.CreateDebtResponse;
import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.model.response.CreateDebtPriorityResponse;
import com.debts.debtsappbackend.model.response.CreateDefaultDebPrioritiesResponse;
import com.debts.debtsappbackend.model.response.GetAllDebsPrioritiesResponse;
import com.debts.debtsappbackend.services.TranslateService;
import com.debts.debtsappbackend.util.DebtPriorityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DebtHelper {
    private final TranslateService translateService;
    private final UserHelper userHelper;

    @Autowired
    public DebtHelper(TranslateService translateService, UserHelper userHelper){
        this.translateService = translateService;
        this.userHelper = userHelper;
    }
    public CreateDebtResponse createDebtResponse(Debt debt) {
        try {
            return new CreateDebtResponse(true, "Debt created successfully", "", debt);
        } catch (Exception e) {
            return new CreateDebtResponse(false, "Error creating debt", e.getMessage(), null);
        }
    }

    public CreateDebtPriorityResponse createDebtPriorityResponse(DebtPriority debtPriority, String error, Locale locale) {
        try {
            if(error != null)
                return new CreateDebtPriorityResponse(false, translateService.getMessage("debt.priority.error.create", locale), error, null);
            return new CreateDebtPriorityResponse(true, translateService.getMessage("debt.priority.success.create", locale), null, _convertDebtPriorityToDto(debtPriority));
        } catch (Exception e) {
            return new CreateDebtPriorityResponse(false, translateService.getMessage("debt.priority.error.create", locale), e.getMessage(), null);
        }
    }

    public CreateDefaultDebPrioritiesResponse createDefaultDebtPrioritiesResponse(List<DebtPriority> debtPriorities, String error, Locale locale) {
        try {
            if(error != null)
                return new CreateDefaultDebPrioritiesResponse(false, translateService.getMessage("debt.create.defaultPriorities.error", locale), error, null);
            return new CreateDefaultDebPrioritiesResponse(true, translateService.getMessage("debt.create.defaultPriorities.success", locale), null, debtPriorities.stream().map(this::_convertDebtPriorityToDto).collect(Collectors.toList()));
        } catch (Exception e) {
            log.info("ERROR: ", e);
            return new CreateDefaultDebPrioritiesResponse(false, translateService.getMessage("debt.create.defaultPriorities.error", locale), e.getMessage(), null);
        }
    }

    public GetAllDebsPrioritiesResponse createGetAllDebtsPrioritiesModel(List<DebtPriority> userDebtPriorities, List<DebtPriority> globalDebtPriorities, String error, Locale locale){
        try{
            if(error != null)
                return new GetAllDebsPrioritiesResponse(false, translateService.getMessage("debt.priority.get.all.error", locale), error, null, null, null);
            List<DebtPriorityDto> userDebtPrioritiesDto = userDebtPriorities.stream().map(this::_convertDebtPriorityToDto).collect(Collectors.toList());
            List<DebtPriorityDto> globalDebtPrioritiesDto = globalDebtPriorities.stream().map(this::_convertDebtPriorityToDto).collect(Collectors.toList());
            List<DebtPriorityDto> allDebtPrioritiesDto = new ArrayList<>();
            allDebtPrioritiesDto.addAll(userDebtPrioritiesDto);
            allDebtPrioritiesDto.addAll(globalDebtPrioritiesDto);
            return new GetAllDebsPrioritiesResponse(true, translateService.getMessage("debt.priority.get.all.success", locale), null, allDebtPrioritiesDto, globalDebtPrioritiesDto, userDebtPrioritiesDto);
        } catch (Exception e) {
            log.info("ERROR: ", e);
            return new GetAllDebsPrioritiesResponse(false, translateService.getMessage("debt.priority.get.all.error", locale), e.getMessage(), null, null, null);
        }
    }

    private DebtPriorityDto _convertDebtPriorityToDto(DebtPriority debtPriority){
        UserDto userDto = debtPriority.getUser() != null ? userHelper.convertToDto(debtPriority.getUser()) : null;
        return new DebtPriorityDto(debtPriority.getId(), debtPriority.getName(), debtPriority.getDescription(), debtPriority.getGlobal(), debtPriority.getColor(), userDto);
    }

    public DebtPriority convertDebtPriorityTypeToDebtPriority(DebtPriorityType debtPriorityType, Locale locale){
        return new DebtPriority(debtPriorityType.getCode(), translateService.getMessage(debtPriorityType.getDescription(), null, locale), true, debtPriorityType.getColor(), null);
    }
}
