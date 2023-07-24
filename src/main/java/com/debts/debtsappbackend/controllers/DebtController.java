package com.debts.debtsappbackend.controllers;

import com.debts.debtsappbackend.helper.DebtHelper;
import com.debts.debtsappbackend.model.DebtCreatedModel;
import com.debts.debtsappbackend.entity.Debt;
import com.debts.debtsappbackend.services.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/debt")
public class DebtController {
    private final DebtHelper debtHelper;
    private final DebtService debtService;

    @Autowired
    public DebtController(DebtHelper debtHelper, DebtService debtService){
        this.debtHelper = debtHelper;
        this.debtService = debtService;
    }

    @PostMapping
    public ResponseEntity<DebtCreatedModel> createDebt(@RequestBody Debt debt) {
        System.out.println("ENTER TO REST CREATE DEBT");
        return ResponseEntity.status(200).body(debtHelper.createDebtResponse(debtService.createDebt(debt)));
    }
}
