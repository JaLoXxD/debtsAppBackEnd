package com.debts.debtsappbackend.controllers;

import com.debts.debtsappbackend.model.request.CreateDebtCategoryRequest;
import com.debts.debtsappbackend.model.request.CreateDebtPriorityRequest;
import com.debts.debtsappbackend.model.request.CreateDebtRequest;
import com.debts.debtsappbackend.model.request.DebtPaymentRequest;
import com.debts.debtsappbackend.model.response.*;
import com.debts.debtsappbackend.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/debt")
@Slf4j
public class DebtController {
    private final DebtService debtService;
    private final DebtPaymentService debtPaymentService;
    private final DebtCategoryService debtCategoryService;
    private final DebtPriorityService debtPriorityService;
    private final ValidatorService validatorService;


    @Autowired
    public DebtController(DebtService debtService, DebtPaymentService debtPaymentService, DebtCategoryService debtCategoryService, DebtPriorityService debtPriorityService, ValidatorService validatorService){
        this.debtService = debtService;
        this.debtPaymentService = debtPaymentService;
        this.debtCategoryService = debtCategoryService;
        this.debtPriorityService = debtPriorityService;
        this.validatorService = validatorService;
    }

    @PostMapping
    public ResponseEntity<DebtResponse> createDebt(@RequestBody CreateDebtRequest createDebtRequest, BindingResult bindingResult, @RequestHeader("Authorization") String token) {
        try{
            log.info("ENTER TO REST CREATE DEBT");
            validatorService.validate("debt", createDebtRequest, bindingResult);
            if(bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(debtService.createDebt(createDebtRequest, token, validatorService.getErrors(bindingResult)));
            }
            DebtResponse debtResponse = debtService.createDebt(createDebtRequest, token, new ArrayList<>());
            HttpStatus status = debtResponse.getErrors() == null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(debtResponse);

        } catch(Exception e) {
            log.error("ERROR {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtService.createDebt(createDebtRequest, token, List.of(e.getMessage())));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<AllDebtsResponse> getAllDebts(@RequestHeader("Authorization") String token) {
        try{
            log.info("ENTER TO REST GET ALL DEBTS");
            return ResponseEntity.status(HttpStatus.OK).body(debtService.getAllDebts(token, new ArrayList<>()));
        } catch(Exception e) {
            log.error("ERROR {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtService.getAllDebts(token, List.of(e.getMessage())));
        }
    }

    @GetMapping("/{debtId}")
    public ResponseEntity<DebtResponse> getDebt(@PathVariable("debtId") Long debtId, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size, @RequestHeader("Authorization") String token) {
        try{
            log.info("ENTER TO REST GET DEBT");
            log.info("PAGE {} SIZE {}", page, size);
            return ResponseEntity.status(HttpStatus.OK).body(debtService.getDebtInfo(debtId, page, size, token, new ArrayList<>()));
        } catch(Exception e) {
            log.error("ERROR {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtService.getDebtInfo(debtId, page, size, token, new ArrayList<>()));
        }
    }

    @PutMapping("/payment/{debtPaymentId}")
    public ResponseEntity<GenericResponse> payDebt(@PathVariable("debtPaymentId") Long debtPaymentId, @RequestBody DebtPaymentRequest request, @RequestHeader("Authorization") String token, BindingResult bindingResult) {
        try{
            log.info("ENTER TO REST PAYMENT");
            validatorService.validate("debt", request, bindingResult);
            if(bindingResult.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(debtPaymentService.updateDebtPayment(request, debtPaymentId, token, validatorService.getErrors(bindingResult)));
            GenericResponse genericResponse = debtPaymentService.updateDebtPayment(request, debtPaymentId, token, new ArrayList<>());
            HttpStatus status = genericResponse.getErrors() == null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(genericResponse);
        } catch(Exception e) {
            log.error("ERROR {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtPaymentService.updateDebtPayment(request, debtPaymentId, token, List.of(e.getMessage())));
        }
    }

    @PostMapping("/category")
    public ResponseEntity<CreateDebtCategoryResponse> createDebtCategory(@RequestBody CreateDebtCategoryRequest request, BindingResult bindingResult, @RequestHeader("Authorization") String token) {
        try{
            log.info("ENTER TO REST CREATE DEBT CATEGORY");
            validatorService.validate("debt", request, bindingResult);
            if(bindingResult.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(debtCategoryService.createDebtCategory(request, token, validatorService.getErrors(bindingResult)));
            return ResponseEntity.status(HttpStatus.OK).body(debtCategoryService.createDebtCategory(request, token, new ArrayList<>()));
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtCategoryService.createDebtCategory(request, token, List.of(e.getMessage())));
        }
    }

    @PostMapping("/priority")
    public ResponseEntity<CreateDebtPriorityResponse> createDebtPriority(@RequestBody CreateDebtPriorityRequest request, BindingResult bindingResult, @RequestHeader("Authorization") String token) {
        try{
            log.info("ENTER TO REST CREATE DEBT PRIORITY");
            validatorService.validate("debt", request, bindingResult);
            if(bindingResult.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(debtPriorityService.createDebtPriority(request, token, validatorService.getErrors(bindingResult)));
            return ResponseEntity.status(HttpStatus.OK).body(debtPriorityService.createDebtPriority(request, token, new ArrayList<>()));
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtPriorityService.createDebtPriority(request, token, List.of(e.getMessage())));
        }
    }

    @GetMapping("/category")
    public ResponseEntity<GetAllDebtsCategoriesResponse> getCategories(@RequestHeader("Authorization") String token) {
        try{
            log.info("ENTER TO REST GET CATEGORY TYPES");
            return ResponseEntity.status(HttpStatus.OK).body(debtCategoryService.getAllDebtCategories(token, new ArrayList<>()));
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtCategoryService.getAllDebtCategories(token, List.of(e.getMessage())));
        }
    }

    @GetMapping("/priority")
    public ResponseEntity<GetAllDebtsPrioritiesResponse> getPriorities(@RequestHeader("Authorization") String token) {
        try{
            log.info("ENTER TO REST GET PRIORITY TYPES");
            return ResponseEntity.status(HttpStatus.OK).body(debtPriorityService.getAllDebtPriorities(token, new ArrayList<>()));
        } catch (Exception e) {
            log.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debtPriorityService.getAllDebtPriorities(token, List.of(e.getMessage())));
        }
    }

    @PostMapping("/default-priorities")
    public ResponseEntity<DefaultDebtPrioritiesResponse> createDefaultDebtPriorities() {
        log.info("ENTER TO REST CREATE DEFAULT DEBT PRIORITIES");
        return ResponseEntity.status(HttpStatus.OK).body(debtPriorityService.createDefaultDebtPriorities());
    }

    @PostMapping("/default-categories")
    public  ResponseEntity<DefaultDebtCategoriesResponse> createDefaultDebtCategories() {
        log.info("ENTER TO REST CREATE DEFAULT DEBT CATEGORIES");
        return ResponseEntity.status(HttpStatus.OK).body(debtCategoryService.createDefaultDebtCategories());
    }
}
