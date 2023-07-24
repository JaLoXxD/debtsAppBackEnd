package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.repository.UserRepository;
import com.debts.debtsappbackend.util.StatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final TranslateService translateService;

    @Autowired
    public UserService(UserRepository userRepository, TranslateService translateService){
        this.userRepository = userRepository;
        this.translateService = translateService;
    }

    public User registerUser(User user){
        return userRepository.save(_setUserDefaultFields(user));
    }

    private User _setUserDefaultFields(User user){
        user.setCreatedAt(LocalDateTime.now());
        log.info("USER CREATED AT: {}", user.getCreatedAt());
        user.setStatus(StatusType.ACTIVE.getCode());
        user.setPassword(_encryptPassword(user.getPassword()));
        return user;
    }

    private String _encryptPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private boolean _isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String isValidUser(User user, Locale locale) {
        User finalUser = _setUserDefaultFields(user);
        if(finalUser.getUserName() == null || finalUser.getUserName().isEmpty()) {
            String fieldName = translateService.getMessage("user.username", null, locale);
            return translateService.getMessage("user.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(finalUser.getPassword() == null || finalUser.getPassword().isEmpty()) {
            String fieldName = translateService.getMessage("user.password", null, locale);
            return translateService.getMessage("user.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(finalUser.getFirstName() == null || finalUser.getFirstName().isEmpty()) {
            String fieldName = translateService.getMessage("user.firstName", null, locale);
            return translateService.getMessage("user.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(finalUser.getLastName() == null || finalUser.getLastName().isEmpty()) {
            String fieldName = translateService.getMessage("user.lastName", null, locale);
            return translateService.getMessage("user.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(finalUser.getEmail() == null || finalUser.getEmail().isEmpty()) {
            String fieldName = translateService.getMessage("user.email", null, locale);
            return translateService.getMessage("user.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(finalUser.getPhone() == null || finalUser.getPhone().isEmpty()) {
            String fieldName = translateService.getMessage("user.phone", null, locale);
            return translateService.getMessage("user.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(finalUser.getCreatedAt() == null) {
            String fieldName = translateService.getMessage("user.createdAt", null, locale);
            return translateService.getMessage("user.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(finalUser.getStatus() == null || finalUser.getStatus().isEmpty()) {
            String fieldName = translateService.getMessage("user.status", null, locale);
            return translateService.getMessage("user.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(!_isValidEmail(finalUser.getEmail())) {
            return translateService.getMessage("user.error.invalidMail", locale);
        }
        return null;
    }
}
