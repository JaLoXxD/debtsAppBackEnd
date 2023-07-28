package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.entity.DebtPriority;
import com.debts.debtsappbackend.repository.DebtPriorityRepository;
import com.debts.debtsappbackend.repository.DebtRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@Slf4j
public class DebtService {
    private final DebtRepository debtRepository;
    private final DebtPriorityRepository debtPriorityRepository;
    private final TranslateService translateService;

    @Autowired
    public DebtService(DebtRepository debtRepository, DebtPriorityRepository debtPriorityRepository, TranslateService translateService){
        this.debtRepository = debtRepository;
        this.debtPriorityRepository = debtPriorityRepository;
        this.translateService = translateService;
    }

    public Debt createDebt(Debt debt) {
        return debtRepository.save(debt);
    }

    public DebtPriority createDebtPriority(DebtPriority debtPriority) {
        return debtPriorityRepository.save(debtPriority);
    }

    public List<DebtPriority> createDefaultDebtPriorities(List<DebtPriority> debtPriorities) {
        return debtPriorityRepository.saveAll(debtPriorities);
    }

    public void deleteDebtPriorityByGlobal(Boolean global) {
        debtPriorityRepository.deleteByGlobal(global);
    }

    public List<DebtPriority> findByUserId(String userId) {
        return debtPriorityRepository.findByUserId(userId);
    }

    public List<DebtPriority> findByGlobal(Boolean global) {
        return debtPriorityRepository.findByGlobal(global);
    }

    public DebtPriority updateDebtPriority(String id, String name, String description, Boolean global, String color) {
        debtPriorityRepository.updateDebtPriority(id, name, description, global, color);
        return debtPriorityRepository.findByName(name);
    }

    public String isValidDebtPriority(DebtPriority debtPriority, Locale locale){
        DebtPriority existingPriority = debtPriorityRepository.findByNameAndGlobal(debtPriority.getName(), true);
        log.info("Existing priority: " + existingPriority);
        if(existingPriority != null){
            return translateService.getMessage("debt.priority.error.alreadyExists", locale);
        }
        if(debtPriority.getName() == null || debtPriority.getName().isEmpty()){
            String fieldName = translateService.getMessage("general.name", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(debtPriority.getDescription() == null || debtPriority.getDescription().isEmpty()){
            String fieldName = translateService.getMessage("general.description", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(debtPriority.getGlobal() == null){
            String fieldName = translateService.getMessage("general.global", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(debtPriority.getColor() == null || debtPriority.getColor().isEmpty()){
            String fieldName = translateService.getMessage("general.color", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }

        return null;
    }
}
