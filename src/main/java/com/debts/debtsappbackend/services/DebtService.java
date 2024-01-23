package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.*;
import com.debts.debtsappbackend.helper.DebtHelper;
import com.debts.debtsappbackend.model.request.CreateDebtRequest;
import com.debts.debtsappbackend.model.response.AllDebtsResponse;
import com.debts.debtsappbackend.model.response.DebtResponse;
import com.debts.debtsappbackend.model.response.GenericResponse;
import com.debts.debtsappbackend.repository.DebtRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
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
        try {
            log.info("CREATE DEBT REQUEST: " + request);
            User user = userService.checkIfUserExists(token, errors);
            DebtCategory debtCategory = null;
            DebtPriority debtPriority = null;
            if (user != null) {
                debtCategory = debtCategoryService.findCategoryById(request.getCategory(), errors);
                debtPriority = debtPriorityService.findPriorityById(request.getPriority(), errors);
                log.info("DEBT CATEGORY: " + debtCategory);
                log.info("DEBT PRIORITY: " + debtPriority);
            }
            log.info("ERRORS: " + errors);
            if (!errors.isEmpty()) {
                return debtHelper.buildDebtResponse(null, null, true, errors);
            }
            Debt debt = debtHelper.mapDebtFromRequest(request, user, debtCategory, debtPriority);
            log.info("DEBT: " + debt);
            Debt newDebt = debtRepository.save(debt);
            _createDebtPayments(request, newDebt);
            return debtHelper.buildDebtResponse(newDebt, null, true, errors);
        } catch (Exception e) {
            log.error("ERROR: " + e);
            if (e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildDebtResponse(null, null, true, errors);
        }
    }

    private void _createDebtPayments(CreateDebtRequest request, Debt newDebt) {
        BigDecimal monthlyAmount = newDebt.getAmount().divide(newDebt.getTermInMonths(), 2, RoundingMode.HALF_UP);
        for (int i = 0; i < request.getTermInMonths().intValue(); i++) {
            String name = request.getName() + " - Payment " + (i + 1);
            debtPaymentService.createDebtPayment(name, request.getStartDate().plusMonths(i + 1), monthlyAmount, newDebt);
        }
    }

    public void updateDebtPendingAmountById(Long debtId, BigDecimal pendingAmount, List<String> errors) {
        try {
            debtRepository.updatePendingAmountById(debtId, pendingAmount);
        } catch (Exception e) {
            log.error("ERROR: ", e);
            if (e.getMessage() != null)
                errors.add(e.getMessage());
        }
    }

    public DebtResponse updateDebt(CreateDebtRequest request, String token, List<String> errors) {
        try {
            log.info("UPDATE DEBT REQUEST: " + request);
            User user = userService.checkIfUserExists(token, errors);
            DebtCategory debtCategory = null;
            DebtPriority debtPriority = null;
            if (user != null) {
                debtCategory = debtCategoryService.findCategoryById(request.getCategory(), errors);
                debtPriority = debtPriorityService.findPriorityById(request.getPriority(), errors);
                log.info("DEBT CATEGORY: " + debtCategory);
                log.info("DEBT PRIORITY: " + debtPriority);
            }
            log.info("ERRORS: " + errors);
            if (!errors.isEmpty()) {
                return debtHelper.buildDebtResponse(null, null, true, errors);
            }
            Debt debt = debtHelper.mapDebtFromRequest(request, user, debtCategory, debtPriority);
            log.info("DEBT: " + debt);
            Debt newDebt = debtRepository.save(debt);
            //delete all payments that are unpaid
            debtPaymentService.deleteAllDebtPaymentsByDebtIdAndPayed(debt.getId(), false);
            _updateDebtPaymentsAfterUpdateDebt(request, newDebt, debtPaymentService.getAllPayedDebtPaymentsByDebtId(debt.getId(), true));
            return debtHelper.buildDebtResponse(newDebt, null, true, errors);
        } catch (Exception e) {
            log.error("ERROR: " + e);
            if (e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildDebtResponse(null, null, true, errors);
        }
    }

    private void _updateDebtPaymentsAfterUpdateDebt(CreateDebtRequest request, Debt newDebt, List<DebtPayment> existingPayments) {
        MathContext mc = new MathContext(3, RoundingMode.DOWN);
        //GET THE TOTAL PAYED AMOUNT
        double payedAmount = existingPayments.stream()
                .mapToDouble(payment ->  payment.getAmount().doubleValue())
                .sum();
        BigDecimal remainingMonths = request.getTermInMonths().subtract(BigDecimal.valueOf(existingPayments.size()));
        BigDecimal remainingAmount = request.getAmount().subtract(BigDecimal.valueOf(payedAmount));
        BigDecimal monthlyAmount = remainingAmount.divide(remainingMonths, mc);
        for (int i = existingPayments.size() - 1; i < request.getTermInMonths().intValue(); i++) {
            String name = request.getName() + " - Payment " + (i + 1);
            debtPaymentService.createDebtPayment(name, request.getStartDate().plusMonths(i + 1), monthlyAmount, newDebt);
        }
    }

    public GenericResponse deleteDebt(Long debtId, String token, List<String> errors) {
        try {
            User user = userService.checkIfUserExists(token, errors);
            Debt debt = debtValidationService.checkIfDebtExists(debtId, errors);
            if (!errors.isEmpty()) {
                return debtHelper.buildGenericResponse("debt.delete.error", errors);
            }
            if (user != null && debt != null) {
                debtPaymentService.deleteAllDebtPaymentsByDebtId(debtId);
                debtRepository.delete(debt);
            }
            return debtHelper.buildGenericResponse("debt.delete.success", errors);
        } catch (Exception e) {
            log.error("ERROR: ", e);
            if (e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildGenericResponse("debt.delete.error", errors);
        }
    }

    public AllDebtsResponse getAllDebts(Integer page, Integer size, String filter, String token, List<String> errors) {
        try {
            User user = userService.checkIfUserExists(token, errors);
            if (!errors.isEmpty()) {
                return debtHelper.buildAllDebtsResponse(null, 0L, 0L, errors);
            }
            int pageNumber = page == null ? 0 : page;
            int pageSize = size == null ? 10 : size;
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
            List<Debt> debts;
            if(filter != null){
                debts = debtRepository.findAllByUserIdAndFilter(user.getId(), filter, pageable).getContent();
            } else{
                debts = debtRepository.findAllByUserId(user.getId(), pageable).getContent();
            }
            Long totalElements = debtRepository.countAllByUserId(user.getId());
            Long totalPages = (long) Math.ceil((double) totalElements / pageSize);
            return debtHelper.buildAllDebtsResponse(debts, totalPages, totalElements, errors);
        } catch (Exception e) {
            log.error("ERROR: ", e);
            if (e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildAllDebtsResponse(null, 0L, 0L, errors);
        }
    }

    public DebtResponse getDebtInfo(Long debtId, Integer page, Integer size, String token, List<String> errors) {
        try {
            userService.checkIfUserExists(token, errors);
            Debt debt = debtValidationService.checkIfDebtExists(debtId, errors);
            log.info("DEBT {}", debt);
            List<DebtPayment> debtPayments = debtPaymentService.getPageAllDebtPaymentsByDebtId(debtId, page, size, null, errors).getContent();
            debt.setDebtPayments(debtPayments);
            log.info("DEBT PAYMENTS {}", debtPayments);
            if (!errors.isEmpty()) {
                return debtHelper.buildDebtResponse(null, debtPayments, false, errors);
            }
            return debtHelper.buildDebtResponse(debt, debtPayments, false, errors);
        } catch (Exception e) {
            log.error("ERROR: ", e);
            if (e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildDebtResponse(null, null, false, errors);
        }
    }
}
