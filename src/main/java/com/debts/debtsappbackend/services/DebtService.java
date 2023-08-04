package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.*;
import com.debts.debtsappbackend.helper.DebtHelper;
import com.debts.debtsappbackend.model.request.CreateDebtCategoryRequest;
import com.debts.debtsappbackend.model.request.CreateDebtPriorityRequest;
import com.debts.debtsappbackend.model.request.CreateDebtRequest;
import com.debts.debtsappbackend.model.request.DebtPaymentRequest;
import com.debts.debtsappbackend.model.response.*;
import com.debts.debtsappbackend.repository.DebtCategoryRepository;
import com.debts.debtsappbackend.repository.DebtPaymentRepository;
import com.debts.debtsappbackend.repository.DebtPriorityRepository;
import com.debts.debtsappbackend.repository.DebtRepository;
import com.debts.debtsappbackend.util.DebtCategoryType;
import com.debts.debtsappbackend.util.DebtPriorityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DebtService {
    private final DebtRepository debtRepository;
    private final DebtPaymentRepository debtPaymentRepository;
    private final DebtPriorityRepository debtPriorityRepository;
    private final DebtCategoryRepository debtCategoryRepository;
    private final TranslateService translateService;
    private final DebtHelper debtHelper;
    private final UserService userService;

    @Autowired
    public DebtService(DebtRepository debtRepository, DebtPaymentRepository debtPaymentRepository, DebtPriorityRepository debtPriorityRepository, DebtCategoryRepository debtCategoryRepository, TranslateService translateService, DebtHelper debtHelper, UserService userService) {
        this.debtRepository = debtRepository;
        this.debtPaymentRepository = debtPaymentRepository;
        this.debtPriorityRepository = debtPriorityRepository;
        this.debtCategoryRepository = debtCategoryRepository;
        this.translateService = translateService;
        this.debtHelper = debtHelper;
        this.userService = userService;
    }

    public DebtResponse createDebt(CreateDebtRequest request, String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            DebtCategory debtCategory = null;
            DebtPriority debtPriority = null;
            if(user != null){
                debtCategory = _findCategoryByIdAnsUserId(request.getCategory(), user.getId(), errors);
                debtPriority = _findPriorityByIdAndUserId(request.getPriority(), user.getId(), errors);
            }
            if(!errors.isEmpty()){
                return debtHelper.buildDebtResponse(null, null, errors);
            }
            Debt debt = debtHelper.mapDebtFromRequest(request, user, debtCategory, debtPriority);
            Debt newDebt = debtRepository.save(debt);
            _createDebtPayments(request, newDebt);
            return debtHelper.buildDebtResponse(newDebt, null, errors);
        } catch(Exception e) {
            log.error("ERROR: " + e);
            if(e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildDebtResponse(null, null, errors);
        }
    }

    public void createDebtPayment(String name, LocalDateTime paymentDate, BigDecimal amount, Debt debt) {
        debtPaymentRepository.save(debtHelper.mapGenericDebtPayment(name, paymentDate, amount, debt));
    }

    private void _createDebtPayments(CreateDebtRequest request, Debt newDebt) {
        BigDecimal monthlyAmount = newDebt.getAmount().divide(newDebt.getTermInMonths(), 2, RoundingMode.HALF_UP);
        for(int i = 0; i < request.getTermInMonths().intValue(); i++){
            String name = request.getName() + " - Payment " + (i + 1);
            createDebtPayment(name, request.getStartDate().plusMonths(i + 1), monthlyAmount, newDebt);
        }
    }

    public GenericResponse updateDebtPayment(DebtPaymentRequest request, String debtPaymentId, String token, List<String> errors) {
        try{
            userService.checkIfUserExists(token, errors);
            _checkIfDebtPaymentExists(debtPaymentId, errors);
            if(!errors.isEmpty()){
                return debtHelper.buildGenericResponse("debt.payment.update.error", errors);
            }
            debtPaymentRepository.updateById(debtPaymentId, request.getName(), request.getDescription(), request.getPaymentDate(), request.getMaxPaymentDate(), request.getAmount(), request.getBalanceAfterPay(), request.getBalanceBeforePay(), request.getImage(), request.getPayed());
            return debtHelper.buildGenericResponse("debt.payment.update.success", errors);
        } catch(Exception e) {
            log.error("ERROR: ", e);
            if(e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildGenericResponse("debt.payment.update.error", errors);
        }
    }

    public AllDebtsResponse getAllDebts(String token, List<String> errors){
        try {
            User user = userService.checkIfUserExists(token, errors);
            if(!errors.isEmpty()){
                return debtHelper.buildAllDebtsResponse(null, errors);
            }
            List<Debt> debts = debtRepository.findAllByUserId(user.getId());
            return debtHelper.buildAllDebtsResponse(debts, errors);
        } catch(Exception e) {
            log.error("ERROR: ", e);
            if(e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildAllDebtsResponse(null, errors);
        }
    }

    public DebtResponse getDebtInfo(String debtId, String token, List<String> errors) {
        try{
            userService.checkIfUserExists(token, errors);
            Debt debt = _checkIfDebtExists(debtId, errors);
            if(!errors.isEmpty()){
                return debtHelper.buildDebtResponse(null, null, errors);
            }
            List<DebtPayment> debtPayments = debtPaymentRepository.findAllByDebtId(debt.getId());
            return debtHelper.buildDebtResponse(debt, debtPayments, errors);
        } catch(Exception e) {
            log.error("ERROR: ", e);
            if(e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildDebtResponse(null, null, errors);
        }
    }

    public CreateDebtPriorityResponse createDebtPriority(CreateDebtPriorityRequest request, String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            _isValidDebtPriority(request, user, errors);
            if(!errors.isEmpty()){
                return debtHelper.buildCreateDebtPriorityResponse(null, errors);
            }
            DebtPriority debtPriority = debtHelper.mapDebtPriorityFromRequest(request);
            debtPriority.setUser(user);
            DebtPriority newDebtPriority = debtPriorityRepository.save(debtPriority);
            return debtHelper.buildCreateDebtPriorityResponse(newDebtPriority, new ArrayList<>());
        } catch(Exception e){
            log.error("ERROR: " + e);
            return debtHelper.buildCreateDebtPriorityResponse(null, List.of(e.getMessage()));
        }
    }

    public CreateDebtCategoryResponse createDebtCategory(CreateDebtCategoryRequest request, String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            _isValidDebtCategory(request, user, errors);
            if(!errors.isEmpty()){
                return debtHelper.buildCreateDebtCategoryResponse(null, errors);
            }
            DebtCategory debtCategory = debtHelper.mapDebtCategoryFromRequest(request);
            debtCategory.setUser(user);
            DebtCategory newDebtCategory = debtCategoryRepository.save(debtCategory);
            return debtHelper.buildCreateDebtCategoryResponse(newDebtCategory, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtHelper.buildCreateDebtCategoryResponse(null, List.of(e.getMessage()));
        }
    }

    public DefaultDebtPrioritiesResponse createDefaultDebtPriorities() {
        try{
            deleteDebtPrioritiesByGlobal(true);
            List<DebtPriority> debtPriorities = Arrays.stream(DebtPriorityType.values()).map(debtHelper::convertDebtPriorityTypeToDebtPriority).collect(Collectors.toList());
            debtPriorityRepository.saveAll(debtPriorities);
            return debtHelper.buildDefaultDebtPrioritiesResponse(debtPriorities, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtHelper.buildDefaultDebtPrioritiesResponse(null, List.of(e.getMessage()));
        }
    }

    public DefaultDebtCategoriesResponse createDefaultDebtCategories() {
        try{
            deleteDebtCategoriesByGlobal(true);
            List<DebtCategory> debtCategories = Arrays.stream(DebtCategoryType.values()).map(debtHelper::convertDebtCategoryTypeToDebtCategory).collect(Collectors.toList());
            debtCategoryRepository.saveAll(debtCategories);
            return debtHelper.buildDefaultDebtCategoriesResponse(debtCategories, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtHelper.buildDefaultDebtCategoriesResponse(null, List.of(e.getMessage()));
        }
    }



    public GetAllDebtsPrioritiesResponse getAllDebtPriorities(String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            if(!errors.isEmpty()){
                return debtHelper.buildGetAllDebtsPrioritiesResponse(null, null, errors);
            }
            List<DebtPriority> userPriorities = debtPriorityRepository.findByUserId(user.getId());
            List<DebtPriority> globalPriorities = debtPriorityRepository.findByGlobal(true);
            return debtHelper.buildGetAllDebtsPrioritiesResponse(userPriorities, globalPriorities, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtHelper.buildGetAllDebtsPrioritiesResponse(null, null, List.of(e.getMessage()));
        }
    }

    public GetAllDebtsCategoriesResponse getAllDebtCategories(String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            if(!errors.isEmpty()){
                return debtHelper.buildGetAllDebtsCategoriesResponse(null, null, errors);
            }
            List<DebtCategory> userCategories = debtCategoryRepository.findByUserId(user.getId());
            List<DebtCategory> globalCategories = debtCategoryRepository.findByGlobal(true);
            return debtHelper.buildGetAllDebtsCategoriesResponse(userCategories, globalCategories, new ArrayList<>());
        } catch(Exception e) {
            log.error("ERROR: " + e);
            return debtHelper.buildGetAllDebtsCategoriesResponse(null, null, List.of(e.getMessage()));
        }
    }

    public void deleteDebtPrioritiesByGlobal(Boolean global) {
        debtPriorityRepository.deleteByGlobal(global);
    }

    public void deleteDebtCategoriesByGlobal(Boolean global) {
        debtCategoryRepository.deleteByGlobal(global);
    }


    private void _isValidDebtPriority(CreateDebtPriorityRequest request, User user, List<String> errors){
        DebtPriority globalExistingPriority = debtPriorityRepository.findByNameAndGlobal(request.getName(), true);
        DebtPriority userExistingPriority = user != null ? debtPriorityRepository.findByNameAndUser(request.getName(), user) : null;
        if(globalExistingPriority != null || userExistingPriority != null){
            errors.add(translateService.getMessage("debt.priority.error.alreadyExists"));
        }
    }

    private void _isValidDebtCategory(CreateDebtCategoryRequest request, User user, List<String> errors){
        DebtCategory globalExistingCategory = debtCategoryRepository.findByNameAndGlobal(request.getName(), true);
        DebtCategory userExistingPriority = user != null ? debtCategoryRepository.findByNameAndUser(request.getName(), user) : null;
        if(globalExistingCategory != null || userExistingPriority != null){
            errors.add(translateService.getMessage("debt.category.error.alreadyExists"));
        }
    }

    private Debt _checkIfDebtExists(String debtId, List<String> errors){
        Debt debt = debtRepository.findById(debtId).orElse(null);
        if(debt == null){
            errors.add(translateService.getMessage("debt.not.found"));
        }
        return debt;
    }

    private DebtPayment _checkIfDebtPaymentExists(String debtPaymentId, List<String> errors){
        DebtPayment debtPayment = debtPaymentRepository.findById(debtPaymentId).orElse(null);
        if(debtPayment == null){
            errors.add(translateService.getMessage("debt.payment.not.found"));
        }
        return debtPayment;
    }

    private DebtCategory _findCategoryByIdAnsUserId(String categoryId, String userId, List<String> errors){
        DebtCategory debtCategory = debtCategoryRepository.findByIdAndUserId(categoryId, userId).orElse(null);
        if(debtCategory == null){
            errors.add(translateService.getMessage("debt.category.not.found"));
        }
        return debtCategory;
    }

    private DebtPriority _findPriorityByIdAndUserId(String priorityId, String userId, List<String> errors){
        DebtPriority debtPriority = debtPriorityRepository.findByIdAndUserId(priorityId, userId).orElse(null);
        if(debtPriority == null){
            errors.add(translateService.getMessage("debt.priority.not.found"));
        }
        return debtPriority;
    }
}
