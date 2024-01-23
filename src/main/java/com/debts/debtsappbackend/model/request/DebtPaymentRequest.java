package com.debts.debtsappbackend.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class DebtPaymentRequest {
    @NotNull
    private Long debtId;
    @NotNull
    private String name;
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime paymentDate;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private BigDecimal pendingAmount;
    private MultipartFile image;
    @NotNull
    private Boolean payed;
}
