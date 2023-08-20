package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.entity.DebtPayment;
import com.debts.debtsappbackend.helper.DebtPaymentHelper;
import com.debts.debtsappbackend.model.request.DebtPaymentRequest;
import com.debts.debtsappbackend.model.response.GenericResponse;
import com.debts.debtsappbackend.repository.DebtPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DebtPaymentService{
    private final DebtPaymentRepository debtPaymentRepository;
    private final DebtPaymentHelper debtPaymentHelper;
    private final UserService userService;
    private final TranslateService translateService;
    private final DebtValidationService debtValidationService;

    public DebtPaymentService(DebtPaymentRepository debtPaymentRepository, DebtPaymentHelper debtPaymentHelper, UserService userService, TranslateService translateService, DebtValidationService debtValidationService) {
        this.debtPaymentRepository = debtPaymentRepository;
        this.debtPaymentHelper = debtPaymentHelper;
        this.userService = userService;
        this.translateService = translateService;
        this.debtValidationService = debtValidationService;
    }

    @Transactional
    public void createDebtPayment(String name, LocalDateTime paymentDate, BigDecimal amount, Debt debt) {
        debtPaymentRepository.save(debtPaymentHelper.mapGenericDebtPayment(name, paymentDate, amount, debt));
    }

    @Transactional
    public GenericResponse updateDebtPayment(DebtPaymentRequest request, Long debtPaymentId, String token, List<String> errors) {
        try{
            userService.checkIfUserExists(token, errors);
            _checkIfDebtPaymentExists(debtPaymentId, errors);
            if(!errors.isEmpty()){
                return debtPaymentHelper.buildGenericResponse(translateService.getMessage("debt.payment.update.error"), errors);
            }
            debtPaymentRepository.updateById(debtPaymentId, request.getName(), request.getDescription(), request.getPaymentDate(), request.getMaxPaymentDate(), request.getAmount(), request.getBalanceAfterPay(), request.getBalanceBeforePay(), request.getImage(), request.getPayed());
            return debtPaymentHelper.buildGenericResponse(translateService.getMessage("debt.payment.update.success"), null);
        } catch(Exception e) {
            log.error("ERROR: ", e);
            if(e.getMessage() != null)
                errors.add(e.getMessage());
            return debtPaymentHelper.buildGenericResponse(translateService.getMessage("debt.payment.update.error"), errors);
        }
    }

    public Page<DebtPayment> getAllDebtPaymentsByDebtId(Long debtId, Integer page, Integer size, List<String> errors) {
        debtValidationService.checkIfDebtExists(debtId, errors);
        log.info("page: {}, size: {}", page, size);
        int pageNumber = page == null ? 0 : page;
        int pageSize = size == null ? 10 : size;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("paymentDate").ascending());
        Page<DebtPayment> debtPayments = debtPaymentRepository.findAllByDebtId(debtId, pageable);
        if(debtPayments.isEmpty()){
            errors.add(translateService.getMessage("debt.payment.not.found"));
        }
        return debtPayments;
    }

    private void _checkIfDebtPaymentExists(Long debtPaymentId, List<String> errors){
        DebtPayment debtPayment = debtPaymentRepository.findById(debtPaymentId).orElse(null);
        if(debtPayment == null){
            errors.add(translateService.getMessage("debt.payment.not.found"));
        }
    }
}
