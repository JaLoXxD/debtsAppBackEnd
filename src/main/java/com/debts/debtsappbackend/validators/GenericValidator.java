package com.debts.debtsappbackend.validators;

import com.debts.debtsappbackend.services.TranslateService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@Setter
public class GenericValidator implements Validator {

    private final TranslateService translateService;

    private String errorType;

    public GenericValidator(TranslateService translateService){
        this.translateService = translateService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors){
        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        log.info("Validating class: " + clazz.getName());
        log.info("Validating class: " + clazz.getSimpleName());
        for(Field field : fields){
            log.info("field: " + field.toString());
            if(Arrays.stream(field.getDeclaredAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(NotNull.class))){
                log.info("Validating field: " + field.getName());
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, field.getName(), getMessage("general.error.emptyField", "'"+ getMessage(errorType +"."+ field.getName(), (Object) null) +"'"));
            }
            //validate email
            if(Arrays.stream(field.getDeclaredAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(Email.class))){
                field.setAccessible(true);
                String email = (String) ReflectionUtils.getField(field, target);
                if(!new EmailValidator().isValid(email, null)){
                    errors.rejectValue(field.getName(), getMessage("general.error.invalidMail", (Object) null));
                }
            }
        }
    }

    public String getMessage(String code, Object... args){
        return translateService.getMessage(code, args);
    }

    public List<String> mapErrors(Errors errors){
        List<String> errorMessages = new ArrayList<>();
        for(ObjectError error : errors.getAllErrors()){
            errorMessages.add(error.getCode());
            log.error("VALIDATION ERROR {}", error.getCode());
        }

        return errorMessages;
    }
}
