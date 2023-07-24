package com.debts.debtsappbackend.controllers;

import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.helper.UserHelper;
import com.debts.debtsappbackend.model.UserCreatedModel;
import com.debts.debtsappbackend.services.TranslateService;
import com.debts.debtsappbackend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserHelper userHelper;
    private final UserService userService;
    private final TranslateService translateService;
    private final LocaleResolver localeResolver;

    @Autowired
    public UserController(UserHelper userHelper, UserService userService, TranslateService translateService, LocaleResolver localeResolver){
        this.userHelper = userHelper;
        this.userService = userService;
        this.translateService = translateService;
        this.localeResolver = localeResolver;
    }


    @PostMapping
    public ResponseEntity<UserCreatedModel> registerUser(@RequestBody User user, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        try {
            logger.info("ENTER USER REGISTER REST WITH LOCALE RESOLVER: {}", locale);
            String errorMsg = userService.isValidUser(user, locale);
            if (errorMsg != null) {
                logger.error("{} {}", translateService.getMessage(errorMsg, locale), user.getEmail());
                return ResponseEntity.status(404).body(userHelper.createUserResponse(new User(), errorMsg, locale));
            }
            return ResponseEntity.status(200).body(userHelper.createUserResponse(userService.registerUser(user), null, null));
        } catch (Exception e) {
            logger.error("ERROR: {}", e);
            return ResponseEntity.status(500).body(userHelper.createUserResponse(new User(), e.getMessage(), locale));
        }
    }
}
