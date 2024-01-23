package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.model.response.GenericResponse;
import com.debts.debtsappbackend.services.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GenericHelper {
    private final TranslateService translateService;

    public GenericHelper(TranslateService translateService){
        this.translateService = translateService;
    }

    public GenericResponse buildGenericResponse(String message, List<String> errors){
        return GenericResponse.builder()
                .success(errors == null || errors.isEmpty())
                .message(translateService.getMessage(message))
                .errors(errors)
                .build();
    }
}
