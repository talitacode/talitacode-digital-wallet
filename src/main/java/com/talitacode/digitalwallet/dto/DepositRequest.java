package com.talitacode.digitalwallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record DepositRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Deposit must be greater than zero")
        @Digits(integer = 12, fraction = 2, message = "Maximum 2 decimal places allowed")
        BigDecimal amount

) {}
