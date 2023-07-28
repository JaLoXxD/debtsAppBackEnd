package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.helper.AuthHelper;
import com.debts.debtsappbackend.helper.UserHelper;
import com.debts.debtsappbackend.model.request.CreateUserRequest;
import com.debts.debtsappbackend.model.response.JwtResponse;
import com.debts.debtsappbackend.model.request.LoginRequest;
import com.debts.debtsappbackend.repository.UserRepository;
import com.debts.debtsappbackend.security.JwtUtil;
import com.debts.debtsappbackend.util.StatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TranslateService translateService;
    private final UserRepository userRepository;
    private final UserHelper userHelper;
    private final AuthHelper authHelper;

    @Autowired
    public UserService(UserRepository userRepository, TranslateService translateService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserHelper userHelper, AuthHelper authHelper) {
        this.userRepository = userRepository;
        this.translateService = translateService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userHelper = userHelper;
        this.authHelper = authHelper;
    }

    public User registerUser(CreateUserRequest request){
        return userRepository.save(_setUserDefaultFields(request));
    }

    public JwtResponse login(LoginRequest request){
        log.debug("ENTER LOGIN");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication);
        return authHelper.generateLoginResponse(token);
    }

    private User _setUserDefaultFields(CreateUserRequest userRequest){
        User user = userHelper.mapUserFromRequest(userRequest);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(StatusType.ACTIVE.getCode());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        log.info("USER THAT WILL BE CREATED: {}", user);
        return user;
    }

    private boolean _isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String isValidUser(CreateUserRequest user, Locale locale) {
        if(user.getUsername() == null || user.getUsername().isEmpty()) {
            String fieldName = translateService.getMessage("user.username", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()) {
            String fieldName = translateService.getMessage("user.password", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(user.getFirstName() == null || user.getFirstName().isEmpty()) {
            String fieldName = translateService.getMessage("user.firstName", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(user.getLastName() == null || user.getLastName().isEmpty()) {
            String fieldName = translateService.getMessage("user.lastName", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            String fieldName = translateService.getMessage("user.email", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(user.getPhone() == null || user.getPhone().isEmpty()) {
            String fieldName = translateService.getMessage("user.phone", null, locale);
            return translateService.getMessage("general.error.emptyField", new Object[] { "'" + fieldName + "'" }, locale);
        }
        if(!_isValidEmail(user.getEmail())) {
            return translateService.getMessage("user.error.invalidMail", locale);
        }
        return null;
    }
}
