package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.entity.DebtPayment;
import com.debts.debtsappbackend.helper.DebtHelper;
import com.debts.debtsappbackend.helper.DebtPaymentHelper;
import com.debts.debtsappbackend.model.request.DebtPaymentRequest;
import com.debts.debtsappbackend.model.request.DebtPaymentRequestParams;
import com.debts.debtsappbackend.model.response.DebtPaymentResponse;
import com.debts.debtsappbackend.model.response.DebtResponse;
import com.debts.debtsappbackend.model.response.GenericResponse;
import com.debts.debtsappbackend.repository.DebtPaymentRepository;
import com.debts.debtsappbackend.repository.DebtRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DebtPaymentService{
    private final DebtPaymentRepository debtPaymentRepository;
    private final DebtRepository debtRepository;
    private final DebtHelper debtHelper;
    private final DebtPaymentHelper debtPaymentHelper;
    private final UserService userService;
    private final TranslateService translateService;
    private final DebtValidationService debtValidationService;
    private final FileService fileService;

    public DebtPaymentService(DebtPaymentRepository debtPaymentRepository, DebtRepository debtRepository, DebtHelper debtHelper, DebtPaymentHelper debtPaymentHelper, UserService userService, TranslateService translateService, DebtValidationService debtValidationService, FileService fileService) {
        this.debtPaymentRepository = debtPaymentRepository;
        this.debtRepository = debtRepository;
        this.debtHelper = debtHelper;
        this.debtPaymentHelper = debtPaymentHelper;
        this.userService = userService;
        this.translateService = translateService;
        this.debtValidationService = debtValidationService;
        this.fileService = fileService;
    }

    @Transactional
    public void createDebtPayment(String name, LocalDateTime paymentDate, BigDecimal amount, Debt debt) {
        debtPaymentRepository.save(debtPaymentHelper.mapGenericDebtPayment(name, paymentDate, amount, debt));
    }

    public List<DebtPayment> getAllPayedDebtPaymentsByDebtId(Long debtId, Boolean payed) {
        return debtPaymentRepository.findAllByDebtIdAndPayed(debtId, payed);
    }

    @Transactional
    public void deleteAllDebtPaymentsByDebtIdAndPayed(Long debtId, Boolean payed) {
        debtPaymentRepository.deleteAllByDebtIdAndPayed(debtId, payed);
    }

    @Transactional
    public void deleteAllDebtPaymentsByDebtId(Long debtId) {
        debtPaymentRepository.deleteAllByDebtId(debtId);
    }

    @Transactional
    public GenericResponse updateDebtPayment(DebtPaymentRequestParams requestParams, MultipartFile image, Long debtPaymentId, String token, List<String> errors) {
        try{
            DebtPaymentRequest request = debtPaymentHelper.mapDebtPaymentRequest(requestParams);
            userService.checkIfUserExists(token, errors);
            DebtPayment currentPayment = _checkIfDebtPaymentExists(debtPaymentId, errors);
            String imageName = image != null ? fileService.uploadFile(image, errors) : null;
            updateDebtPendingAmountById(request.getDebtId(), request.getPendingAmount(), errors);
            if(!errors.isEmpty()){
                return debtPaymentHelper.buildGenericResponse(translateService.getMessage("debt.payment.update.error"), errors);
            }
            assert currentPayment != null;
            BigDecimal balanceAfterPay = request.getPendingAmount().subtract(request.getAmount());
            debtPaymentRepository.updateById(debtPaymentId, request.getName(), request.getDescription(), request.getPaymentDate(), request.getAmount(), balanceAfterPay, request.getPendingAmount(), imageName, request.getPayed());
            return debtPaymentHelper.buildGenericResponse(translateService.getMessage("debt.payment.update.success"), null);
        } catch(Exception e) {
            log.error("ERROR: ", e);
            if(e.getMessage() != null)
                errors.add(e.getMessage());
            return debtPaymentHelper.buildGenericResponse(translateService.getMessage("debt.payment.update.error"), errors);
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

    public Page<DebtPayment> getPageAllDebtPaymentsByDebtId(Long debtId, Integer page, Integer size, String filter, List<String> errors) {
        debtValidationService.checkIfDebtExists(debtId, errors);
        int pageNumber = page == null ? 0 : page;
        int pageSize = size == null ? 10 : size;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("paymentDate").ascending());
        Page<DebtPayment> debtPayments;
        log.info("FILTER {}", filter);
        if (filter != null) {
            debtPayments = debtPaymentRepository.findAllByDebtIdAndFilter(debtId, filter, pageable);
        } else {
            debtPayments = debtPaymentRepository.findAllByDebtId(debtId, pageable);
        }
        if(debtPayments.isEmpty()){
            errors.add(translateService.getMessage("debt.payment.not.found"));
        }
        return debtPayments;
    }

    public DebtPaymentResponse getAllDebtPaymentsByDebtId(Long debtId, Integer page, Integer size, String filter, String token, List<String> errors) {
        try {
            userService.checkIfUserExists(token, errors);
            Debt debt = debtValidationService.checkIfDebtExists(debtId, errors);
            log.info("DEBT {}", debt);
            List<DebtPayment> debtPayments = getPageAllDebtPaymentsByDebtId(debtId, page, size, filter, errors).getContent();
            log.info("debtPayments {}", debtPayments);
            int pageSize = size == null ? 10 : size;
            Long totalElements = debtPaymentRepository.countAllByDebtId(debtId);
            Long totalPages = (long) Math.ceil((double) totalElements / pageSize);
            debt.setDebtPayments(debtPayments);
            log.info("DEBT PAYMENTS {}", debtPayments);
            if (!errors.isEmpty()) {
                return debtHelper.buildDebtPaymentResponse(null, debtPayments, totalElements, totalPages, errors);
            }
            return debtHelper.buildDebtPaymentResponse(debt, debtPayments, totalElements, totalPages, errors);
        } catch (Exception e) {
            log.error("ERROR: ", e);
            if (e.getMessage() != null)
                errors.add(e.getMessage());
            return debtHelper.buildDebtPaymentResponse(null, null, 0L, 0L, errors);
        }
    }

    private DebtPayment _checkIfDebtPaymentExists(Long debtPaymentId, List<String> errors){
        DebtPayment debtPayment = debtPaymentRepository.findById(debtPaymentId).orElse(null);
        if(debtPayment == null){
            errors.add(translateService.getMessage("debt.payment.not.found"));
            return null;
        }
        return debtPayment;
    }
}
