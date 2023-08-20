package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.DebtPriority;
import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.helper.DebtPriorityHelper;
import com.debts.debtsappbackend.model.request.CreateDebtPriorityRequest;
import com.debts.debtsappbackend.model.response.CreateDebtPriorityResponse;
import com.debts.debtsappbackend.model.response.DefaultDebtPrioritiesResponse;
import com.debts.debtsappbackend.model.response.GetAllDebtsPrioritiesResponse;
import com.debts.debtsappbackend.repository.DebtPriorityRepository;
import com.debts.debtsappbackend.util.DebtPriorityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DebtPriorityService {
    private final DebtPriorityRepository debtPriorityRepository;
    private final DebtPriorityHelper debtPriorityHelper;
    private final UserService userService;
    private final TranslateService translateService;

    public DebtPriorityService(DebtPriorityRepository debtPriorityRepository, DebtPriorityHelper debtPriorityHelper, UserService userService, TranslateService translateService) {
        this.debtPriorityRepository = debtPriorityRepository;
        this.debtPriorityHelper = debtPriorityHelper;
        this.userService = userService;
        this.translateService = translateService;
    }


    @Transactional
    public CreateDebtPriorityResponse createDebtPriority(CreateDebtPriorityRequest request, String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            _isValidDebtPriority(request, user, errors);
            if(!errors.isEmpty()){
                return debtPriorityHelper.buildCreateDebtPriorityResponse(null, errors);
            }
            DebtPriority debtPriority = debtPriorityHelper.mapDebtPriorityFromRequest(request);
            debtPriority.setUser(user);
            DebtPriority newDebtPriority = debtPriorityRepository.save(debtPriority);
            return debtPriorityHelper.buildCreateDebtPriorityResponse(newDebtPriority, new ArrayList<>());
        } catch(Exception e){
            log.error("ERROR: " + e);
            return debtPriorityHelper.buildCreateDebtPriorityResponse(null, List.of(e.getMessage()));
        }
    }

    @Transactional
    public DefaultDebtPrioritiesResponse createDefaultDebtPriorities() {
        try{
            _deleteDebtPrioritiesByGlobal(true);
            List<DebtPriority> debtPriorities = Arrays.stream(DebtPriorityType.values()).map(debtPriorityHelper::convertDebtPriorityTypeToDebtPriority).collect(Collectors.toList());
            debtPriorityRepository.saveAll(debtPriorities);
            return debtPriorityHelper.buildDefaultDebtPrioritiesResponse(debtPriorities, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtPriorityHelper.buildDefaultDebtPrioritiesResponse(null, List.of(e.getMessage()));
        }
    }

    public GetAllDebtsPrioritiesResponse getAllDebtPriorities(String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            if(!errors.isEmpty()){
                return debtPriorityHelper.buildGetAllDebtsPrioritiesResponse(null, null, errors);
            }
            List<DebtPriority> userPriorities = debtPriorityRepository.findByUserId(user.getId());
            List<DebtPriority> globalPriorities = debtPriorityRepository.findByGlobal(true);
            return debtPriorityHelper.buildGetAllDebtsPrioritiesResponse(userPriorities, globalPriorities, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtPriorityHelper.buildGetAllDebtsPrioritiesResponse(null, null, List.of(e.getMessage()));
        }
    }

    private void _deleteDebtPrioritiesByGlobal(Boolean global) {
        debtPriorityRepository.deleteByGlobal(global);
    }


    private void _isValidDebtPriority(CreateDebtPriorityRequest request, User user, List<String> errors){
        DebtPriority globalExistingPriority = debtPriorityRepository.findByNameAndGlobal(request.getName(), true);
        DebtPriority userExistingPriority = user != null ? debtPriorityRepository.findByNameAndUser(request.getName(), user) : null;
        if(globalExistingPriority != null || userExistingPriority != null){
            errors.add(translateService.getMessage("debt.priority.error.alreadyExists"));
        }
    }

    public DebtPriority findPriorityById(Long priorityId, List<String> errors){
        DebtPriority debtPriority = debtPriorityRepository.findById(priorityId).orElse(null);
        if(debtPriority == null){
            errors.add(translateService.getMessage("debt.priority.not.found"));
        }
        return debtPriority;
    }
}
