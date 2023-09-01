package com.debts.debtsappbackend.services;

import com.debts.debtsappbackend.entity.User;
import com.debts.debtsappbackend.helper.AuthHelper;
import com.debts.debtsappbackend.helper.UserHelper;
import com.debts.debtsappbackend.model.request.*;
import com.debts.debtsappbackend.model.response.GenericResponse;
import com.debts.debtsappbackend.model.response.JwtResponse;
import com.debts.debtsappbackend.model.response.RecoverPasswordResponse;
import com.debts.debtsappbackend.model.response.UserResponse;
import com.debts.debtsappbackend.repository.UserRepository;
import com.debts.debtsappbackend.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserHelper userHelper;
    private final AuthHelper authHelper;
    private final TranslateService translateService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserHelper userHelper, AuthHelper authHelper, TranslateService translateService, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userHelper = userHelper;
        this.authHelper = authHelper;
        this.translateService = translateService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    private List<String> _checkIfUserExists(CreateUserRequest request){
        List<String> errors = new ArrayList<>();
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            errors.add(translateService.getMessage("user.email.already.exists"));
        }
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            errors.add(translateService.getMessage("user.username.already.exists"));
        }
        return errors;
    }

    public UserResponse registerUser(CreateUserRequest request, List<String> errors){
        List<String> existingUserErrors = _checkIfUserExists(request != null ? request : new CreateUserRequest());
        if(!existingUserErrors.isEmpty()){
            return userHelper.createUserResponse(null, "create", existingUserErrors);
        }

        User newUser = errors != null ? new User() : userRepository.save(userHelper.setUserDefaultFields(request));
        return userHelper.createUserResponse(newUser, "create", errors);
    }

    @Transactional
    public JwtResponse login(LoginRequest request, List<String> errors){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if(!errors.isEmpty()){
                return authHelper.generateLoginResponse(null, errors);
            }
            String token = jwtUtil.generateToken(authentication);
            log.info("authentication {}", authentication);
            userRepository.findByUsername(request.getUsername()).ifPresent(user -> userRepository.updateLastLoginById(user.getId(), LocalDateTime.now()));
            return authHelper.generateLoginResponse(token, null);
        } catch (Exception e){
            log.error("ERROR MSG {}", e.getMessage());
            return authHelper.generateLoginResponse(null, _mapExceptionToErrors(e, errors));
        }
    }

    @Transactional
    public RecoverPasswordResponse recoverPassword(RecoverPasswordRequest request, List<String> errors){
        try{
            if(!errors.isEmpty()){
                return authHelper.generateRecoverPasswordResponse(null, errors);
            }
            User user = userRepository.findByEmailOrUsername(request.getUserId()).orElse(null);
            if(user == null){
                errors.add(translateService.getMessage("user.email.not.found"));
                return authHelper.generateRecoverPasswordResponse(null, errors);
            }
            String newPassword = authHelper.generateRandomPassword(8);
            mailService.sendMail(user.getEmail(), "Recover password", "Your password is: " + newPassword);
            _recoverPasswordByEmail(user.getEmail(), newPassword);
            return authHelper.generateRecoverPasswordResponse(user.getEmail(), errors);
        } catch (Exception e){
            log.error("ERROR MSG {}", e.getMessage());
            return authHelper.generateRecoverPasswordResponse(null, _mapExceptionToErrors(e, errors));
        }
    }

    @Transactional
    public GenericResponse updatePassword(UpdatePasswordRequest request, String token, List<String> errors){
        try{
            User user = checkIfUserExists(token, errors);
            if(user != null && !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
                errors.add(translateService.getMessage("user.password.current.error"));
            }
            if(!request.getNewPassword().equals(request.getConfirmPassword())){
                errors.add(translateService.getMessage("user.password.confirm.error"));
            }
            if(!errors.isEmpty()){
                return authHelper.buildGenericResponse(translateService.getMessage("user.password.update.error"), errors);
            }
            _updatePasswordById(Objects.requireNonNull(user).getId(), request.getNewPassword());
            return authHelper.buildGenericResponse(translateService.getMessage("user.password.update.success"), null);
        } catch (Exception e){
            log.error("ERROR MSG {}", e.getMessage());
            return authHelper.buildGenericResponse(translateService.getMessage("user.password.update.error"), _mapExceptionToErrors(e, errors));
        }
    }

    @Transactional
    public GenericResponse updateUser(UpdateUserRequest request, String token, List<String> errors){
        try{
            User user = checkIfUserExists(token, errors);
            if(!errors.isEmpty()){
                return authHelper.buildGenericResponse(translateService.getMessage("user.update.error"), errors);
            }
            userRepository.updateUserById(user.getId(), request.getFirstName(), request.getLastName(), request.getSecondName(), request.getSecondLastName(), request.getEmail(), LocalDateTime.now(), request.getPhone(), request.getSalary());
            return authHelper.buildGenericResponse(translateService.getMessage("user.update.success"), null);
        } catch (Exception e){
            log.error("ERROR MSG {}", e.getMessage());
            return authHelper.buildGenericResponse(translateService.getMessage("user.update.error"), _mapExceptionToErrors(e, errors));
        }
    }

    public UserResponse getUserData(String token, List<String> errors){
        User user = checkIfUserExists(token, errors);
        if(user == null){
            return userHelper.createUserResponse(null, "get", errors);
        }
        return userHelper.createUserResponse(user, "get", null);
    }

    public User checkIfUserExists(String token, List<String> errors){
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null){
            errors.add(translateService.getMessage("user.not.found"));
        }
        return user;
    }

    private List<String> _mapExceptionToErrors(Exception e, List<String> errors){
        List<String> errorList = new ArrayList<>();
        if(e.getMessage() != null)
            errorList.add(translateService.getMessage(e.getMessage().replace(" ",".").toLowerCase()));
        if(errors != null)
            errorList.addAll(errors);
        log.info("ERROR LIST {}", errorList);
        return errorList;
    }

    private void _recoverPasswordByEmail(String email, String password){
        userRepository.recoverUserPasswordByEmail(email, passwordEncoder.encode(password));
    }

    private void _updatePasswordById(Long userId, String password) {
        userRepository.updateUserPasswordById(userId, passwordEncoder.encode(password));
    }
}
