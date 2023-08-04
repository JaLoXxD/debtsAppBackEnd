package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.validators.GenericValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@Slf4j
public class ValidatorService {
    private final GenericValidator genericValidator;

    public ValidatorService(GenericValidator genericValidator) {
        this.genericValidator = genericValidator;
    }

    public void validate(String errorType, Object object, BindingResult bindingResult) {
        genericValidator.setErrorType(errorType);
        genericValidator.validate(object, bindingResult);
    }

    public List<String> getErrors(BindingResult bindingResult){
        return genericValidator.mapErrors(bindingResult);
    }
}
