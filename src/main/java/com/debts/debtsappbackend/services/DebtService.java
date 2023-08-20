package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.*;
import com.debts.debtsappbackend.helper.DebtHelper;
import com.debts.debtsappbackend.model.request.CreateDebtRequest;
import com.debts.debtsappbackend.model.response.AllDebtsResponse;
import com.debts.debtsappbackend.model.response.DebtResponse;
import com.debts.debtsappbackend.repository.DebtRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
public class DebtService {
    private final DebtRepository debtRepository;
    private final DebtHelper debtHelper;
    private final UserService userService;
    private final DebtPaymentService debtPaymentService;
    private final DebtCategoryService debtCategoryService;
    private final DebtPriorityService debtPriorityService;
    private final DebtValidationService debtValidationService;

    @Autowired
    public DebtService(DebtRepository debtRepository, DebtHelper debtHelper, UserService userService, DebtPaymentService debtPaymentService, DebtCategoryService debtCategoryService, DebtPriorityService debtPriorityService, DebtValidationService debtValidationService) {
        this.debtRepository = debtRepository;
        this.debtHelper = debtHelper;
        this.userService = userService;
        this.debtPaymentService = debtPaymentService;
        this.debtCategoryService = debtCategoryService;
        this.debtPriorityService = debtPriorityService;
        this.debtValidationService = debtValidationService;
    }

    public DebtResponse createDebt(CreateDebtRequest request, String token, List<String> errors) {
        try{
            User user = userService.checkIfUserExists(token, errors);
            DebtCategory debtCategory = null;
            DebtPriority debtPriority = null;
            if(user != null){
                debtCategory = debtCategoryService.findCategoryById(request.getCategory(), errors);
                debtPriority = debtPriorityService.findPriorityById(request.getPriority(), errors);
                log.info("DEBT CATEGORY: " + debtCategory);
                log.info("DEBT PRIORITY: " + debtPriority);
            }
            log.info("ERRORS: " + errors);
            if(!errors.isEmpty()){
                return debtHelper.buildDebtResponse(null, null, true, errors);
            }
            Debt debt = debtHelper.mapDebtFromRequest(request, user, debtCategory, debtPriority);
            log.info("DEBT: " + debt);
            Debt newDebt = debtRepository.save(debt);
            _createDebtPayments(request, newDebt);
            return debtHelper.buildDebtResponse(newDebt, null, true, errors);
        } catch(Exception e) {
            log.error("ERROR: " + e);
            if(e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildDebtResponse(null, null, true, errors);
        }
    }

    private void _createDebtPayments(CreateDebtRequest request, Debt newDebt) {
        BigDecimal monthlyAmount = newDebt.getAmount().divide(newDebt.getTermInMonths(), 2, RoundingMode.HALF_UP);
        for(int i = 0; i < request.getTermInMonths().intValue(); i++){
            String name = request.getName() + " - Payment " + (i + 1);
            debtPaymentService.createDebtPayment(name, request.getStartDate().plusMonths(i + 1), monthlyAmount, newDebt);
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

    public DebtResponse getDebtInfo(Long debtId, Integer page, Integer size, String token, List<String> errors) {
        try{
            userService.checkIfUserExists(token, errors);
            Debt debt = debtValidationService.checkIfDebtExists(debtId, errors);
            log.info("DEBT {}", debt);
            List<DebtPayment> debtPayments = debtPaymentService.getAllDebtPaymentsByDebtId(debtId, page, size, errors).getContent();
            debt.setDebtPayments(debtPayments);
            log.info("DEBT PAYMENTS {}", debtPayments);
            if(!errors.isEmpty()){
                return debtHelper.buildDebtResponse(null, debtPayments, false, errors);
            }
            return debtHelper.buildDebtResponse(debt, debtPayments, false, errors);
        } catch(Exception e) {
            log.error("ERROR: ", e);
            if(e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildDebtResponse(null, null, false, errors);
        }
    }
}
