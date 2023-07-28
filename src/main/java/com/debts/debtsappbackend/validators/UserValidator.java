package com.debts.debtsappbackend.validators;

import com.debts.debtsappbackend.model.response.CreateUserResponse;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
public class UserValidator implements Validator {

    private final MessageSource messageSource;

    public UserValidator(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CreateUserResponse.class.equals(aClass);
    }

    @Override
    public void validate(Object tarjet, Errors errors){
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
    }

    public String getMessage(String code){
        return messageSource.getMessage(code, null, new Locale("es"));
    }
}
