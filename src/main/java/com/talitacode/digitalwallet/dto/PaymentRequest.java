package com.talitacode.digitalwallet.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PaymentRequest(

        @NotNull(message = "Amount is required")
        @Digits(integer = 12, fraction = 2, message = "Maximum 2 decimal places allowed")
        BigDecimal amount
) {}
