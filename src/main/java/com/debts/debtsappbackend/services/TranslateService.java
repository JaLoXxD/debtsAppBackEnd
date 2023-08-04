package com.debts.debtsappbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Service
public class TranslateService {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;

    @Autowired
    public TranslateService(MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.request = request;
    }

    public String getMessage(String key) {
        try{
            return messageSource.getMessage(key, null, localeResolver.resolveLocale(request));
        }catch (NoSuchMessageException e){
            return key;
        }
    }

    public String getMessage(String key, Object[] args){
        try{
            return messageSource.getMessage(key, args, localeResolver.resolveLocale(request));
        }catch (NoSuchMessageException e){
            return key;
        }
    }
}
