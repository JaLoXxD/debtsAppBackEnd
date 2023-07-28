package com.debts.debtsappbackend.controllers;

import com.debts.debtsappbackend.entity.DebtPriority;
import com.debts.debtsappbackend.helper.DebtHelper;
import com.debts.debtsappbackend.model.response.CreateDebtResponse;
import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.model.response.CreateDebtPriorityResponse;
import com.debts.debtsappbackend.model.response.CreateDefaultDebPrioritiesResponse;
import com.debts.debtsappbackend.model.response.GetAllDebsPrioritiesResponse;
import com.debts.debtsappbackend.services.DebtService;
import com.debts.debtsappbackend.util.DebtPriorityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/v1/debt")
@Slf4j
public class DebtController {
    private final DebtHelper debtHelper;
    private final DebtService debtService;
    private final LocaleResolver localeResolver;


    @Autowired
    public DebtController(DebtHelper debtHelper, DebtService debtService, LocaleResolver localeResolver){
        this.debtHelper = debtHelper;
        this.debtService = debtService;
        this.localeResolver = localeResolver;
    }

    @PostMapping
    public ResponseEntity<CreateDebtResponse> createDebt(@RequestBody Debt debt) {
        System.out.println("ENTER TO REST CREATE DEBT");
        return ResponseEntity.status(HttpStatus.OK).body(debtHelper.createDebtResponse(debtService.createDebt(debt)));
    }

    @PostMapping("/priority")
    public ResponseEntity<CreateDebtPriorityResponse> createDebtPriority(@RequestBody DebtPriority debtPriority, HttpServletRequest request) {
        log.info("ENTER TO REST CREATE DEBT PRIORITY");
        Locale locale = localeResolver.resolveLocale(request);
        try{
            String errorMsg = debtService.isValidDebtPriority(debtPriority, locale);
            if(errorMsg != null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(debtHelper.createDebtPriorityResponse(new DebtPriority(), errorMsg, locale));
            return ResponseEntity.status(HttpStatus.OK).body(debtHelper.createDebtPriorityResponse(debtService.createDebtPriority(debtPriority), null, locale));
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtHelper.createDebtPriorityResponse(new DebtPriority(), e.getMessage(), locale));
        }
    }

    @PostMapping("/defaultPriorities")
    public ResponseEntity<CreateDefaultDebPrioritiesResponse> createDefaultDebtPriorities(HttpServletRequest request) {
        log.info("ENTER TO REST CREATE DEFAULT DEBT PRIORITIES");
        Locale locale = localeResolver.resolveLocale(request);
        try{
            debtService.deleteDebtPriorityByGlobal(true);
            List<DebtPriority> debtPriorities = Arrays.stream(DebtPriorityType.values()).map(debtPriorityType -> debtHelper.convertDebtPriorityTypeToDebtPriority(debtPriorityType, locale)).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(debtHelper.createDefaultDebtPrioritiesResponse(debtService.createDefaultDebtPriorities(debtPriorities), null, locale));
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtHelper.createDefaultDebtPrioritiesResponse(null, e.getMessage(), locale));
        }
    }

    @GetMapping("/priority")
    public ResponseEntity<GetAllDebsPrioritiesResponse> getPriorityTypes(HttpServletRequest request) {
        log.info("ENTER TO REST GET PRIORITY TYPES");
        Locale locale = localeResolver.resolveLocale(request);
        try{
            //TODO: SET USER ID
            return ResponseEntity.status(HttpStatus.OK).body(debtHelper.createGetAllDebtsPrioritiesModel(debtService.findByUserId(""), debtService.findByGlobal(true), null, locale));
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtHelper.createGetAllDebtsPrioritiesModel(null, null,  e.getMessage(), locale));
        }
    }
}
