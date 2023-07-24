package com.debts.debtsappbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TranslateService {

    private final MessageSource messageSource;

    @Autowired
    public TranslateService(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Locale locale) {
        try{
            return messageSource.getMessage(key, null, locale);
        }catch (NoSuchMessageException e){
            return key;
        }
    }

    public String getMessage(String key, Object[] args, Locale locale){
        try{
            return messageSource.getMessage(key, args, locale);
        }catch (NoSuchMessageException e){
            return key;
        }
    }
}
