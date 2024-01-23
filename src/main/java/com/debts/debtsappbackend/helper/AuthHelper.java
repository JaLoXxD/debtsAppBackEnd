package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.model.response.JwtResponse;
import com.debts.debtsappbackend.model.response.RecoverPasswordResponse;
import com.debts.debtsappbackend.services.TranslateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class AuthHelper extends GenericHelper{

    TranslateService translateService;

    public AuthHelper(TranslateService translateService) {
        super(translateService);
        this.translateService = translateService;
    }

    public JwtResponse generateLoginResponse(String token, List<String> errors){
        return JwtResponse.builder()
                .success(token != null)
                .message(token != null ? translateService.getMessage("user.login.success") : translateService.getMessage("user.login.error"))
                .errors(token == null ? errors : null)
                .token(token)
                .build();
    }

    public RecoverPasswordResponse generateRecoverPasswordResponse(String email, List<String> errors){
        log.info("Recover password response: " + errors);
        return RecoverPasswordResponse.builder()
                .success(Objects.requireNonNull(errors).isEmpty())
                .message(Objects.requireNonNull(errors).isEmpty() ? translateService.getMessage("user.email.sent.success") : translateService.getMessage("user.email.sent.error"))
                .errors(Objects.requireNonNull(errors).isEmpty() ? null : errors)
                .email(email)
                .build();
    }

    public String generateRandomPassword(int length) {
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charset.length());
            char randomChar = charset.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
