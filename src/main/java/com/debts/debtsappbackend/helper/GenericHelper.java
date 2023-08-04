package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.model.response.GenericResponse;

import java.util.List;

public class GenericHelper {
    public GenericResponse buildGenericResponse(String message, List<String> errors){
        return GenericResponse.builder()
                .success(errors == null)
                .message(message)
                .errors(errors)
                .build();
    }
}
