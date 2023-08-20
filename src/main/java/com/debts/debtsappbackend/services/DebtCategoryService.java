package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.DebtCategory;
import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.helper.DebtCategoryHelper;
import com.debts.debtsappbackend.model.request.CreateDebtCategoryRequest;
import com.debts.debtsappbackend.model.response.CreateDebtCategoryResponse;
import com.debts.debtsappbackend.model.response.DefaultDebtCategoriesResponse;
import com.debts.debtsappbackend.model.response.GetAllDebtsCategoriesResponse;
import com.debts.debtsappbackend.repository.DebtCategoryRepository;
import com.debts.debtsappbackend.util.DebtCategoryType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DebtCategoryService {
    private final DebtCategoryRepository debtCategoryRepository;
    private final DebtCategoryHelper debtCategoryHelper;
    private final TranslateService translateService;
    private final UserService userService;

    public DebtCategoryService(DebtCategoryRepository debtCategoryRepository, DebtCategoryHelper debtCategoryHelper, TranslateService translateService, UserService userService) {
        this.debtCategoryRepository = debtCategoryRepository;
        this.debtCategoryHelper = debtCategoryHelper;
        this.translateService = translateService;
        this.userService = userService;
    }

    @Transactional
    public CreateDebtCategoryResponse createDebtCategory(CreateDebtCategoryRequest request, String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            _isValidDebtCategory(request, user, errors);
            if(!errors.isEmpty()){
                return debtCategoryHelper.buildCreateDebtCategoryResponse(null, errors);
            }
            DebtCategory debtCategory = debtCategoryHelper.mapDebtCategoryFromRequest(request);
            debtCategory.setUser(user);
            DebtCategory newDebtCategory = debtCategoryRepository.save(debtCategory);
            return debtCategoryHelper.buildCreateDebtCategoryResponse(newDebtCategory, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtCategoryHelper.buildCreateDebtCategoryResponse(null, List.of(e.getMessage()));
        }
    }

    @Transactional
    public DefaultDebtCategoriesResponse createDefaultDebtCategories() {
        try{
            _deleteDebtCategoriesByGlobal(true);
            List<DebtCategory> debtCategories = Arrays.stream(DebtCategoryType.values()).map(debtCategoryHelper::convertDebtCategoryTypeToDebtCategory).collect(Collectors.toList());
            debtCategoryRepository.saveAll(debtCategories);
            return debtCategoryHelper.buildDefaultDebtCategoriesResponse(debtCategories, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtCategoryHelper.buildDefaultDebtCategoriesResponse(null, List.of(e.getMessage()));
        }
    }

    public GetAllDebtsCategoriesResponse getAllDebtCategories(String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            if(!errors.isEmpty()){
                return debtCategoryHelper.buildGetAllDebtsCategoriesResponse(null, null, errors);
            }
            List<DebtCategory> userCategories = debtCategoryRepository.findByUserId(user.getId());
            List<DebtCategory> globalCategories = debtCategoryRepository.findByGlobal(true);
            return debtCategoryHelper.buildGetAllDebtsCategoriesResponse(userCategories, globalCategories, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtCategoryHelper.buildGetAllDebtsCategoriesResponse(null, null, List.of(e.getMessage()));
        }
    }

    private void _deleteDebtCategoriesByGlobal(Boolean global) {
        debtCategoryRepository.deleteByGlobal(global);
    }


    private void _isValidDebtCategory(CreateDebtCategoryRequest request, User user, List<String> errors){
        DebtCategory globalExistingCategory = debtCategoryRepository.findByNameAndGlobal(request.getName(), true);
        DebtCategory userExistingPriority = user != null ? debtCategoryRepository.findByNameAndUserId(request.getName(), user.getId()) : null;
        if(globalExistingCategory != null || userExistingPriority != null){
            errors.add(translateService.getMessage("debt.category.error.alreadyExists"));
        }
    }

    public DebtCategory findCategoryById(Long categoryId, List<String> errors){
        DebtCategory debtCategory = debtCategoryRepository.findById(categoryId).orElse(null);
        if(debtCategory == null){
            errors.add(translateService.getMessage("debt.category.not.found"));
        }
        return debtCategory;
    }
}
