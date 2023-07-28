package com.debts.debtsappbackend.controllers;

import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.helper.UserHelper;
import com.debts.debtsappbackend.model.request.CreateUserRequest;
import com.debts.debtsappbackend.model.response.CreateUserResponse;
import com.debts.debtsappbackend.services.TranslateService;
import com.debts.debtsappbackend.services.UserService;
import com.debts.debtsappbackend.validators.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
@RequestMapping("/api/public/v1/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserHelper userHelper;
    private final UserService userService;
    private final TranslateService translateService;
    private final LocaleResolver localeResolver;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserHelper userHelper, UserService userService, TranslateService translateService, LocaleResolver localeResolver, UserValidator userValidator) {
        this.userHelper = userHelper;
        this.userService = userService;
        this.translateService = translateService;
        this.localeResolver = localeResolver;
        this.userValidator = userValidator;
    }


    @PostMapping
    public ResponseEntity<CreateUserResponse> registerUser(@RequestBody CreateUserRequest user, HttpServletRequest request, BindingResult bindingResult) {
        Locale locale = localeResolver.resolveLocale(request);
        try {
            logger.info("ENTER USER REGISTER REST WITH LOCALE RESOLVER: {}", locale);
            userValidator.validate(user, bindingResult);
            if(bindingResult.hasErrors()){
                logger.error("ERROR: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userHelper.createUserResponse(new User(), translateService.getMessage("user.validation.error", locale), locale));
            }
            String errorMsg = userService.isValidUser(user, locale);
            if (errorMsg != null) {
                logger.error("{} {}", translateService.getMessage(errorMsg, locale), user.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userHelper.createUserResponse(new User(), errorMsg, locale));
            }
            return ResponseEntity.status(HttpStatus.OK).body(userHelper.createUserResponse(userService.registerUser(user), null, locale));
        } catch (Exception e) {
            logger.error("ERROR:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userHelper.createUserResponse(new User(), e.getMessage(), locale));
        }
    }
}
