package com.talitacode.digitalwallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.talitacode.digitalwallet.validation.CPF;
public record UserRegisterRequest(

        @NotBlank(message = "Full name is required")
        String fullName,

        @NotBlank(message = "CPF is required")
        @Size(min = 11, max = 11, message = "CPF must have 11 digits")
        @CPF
        String cpf,

        @NotBlank(message = "Login is required")
        String login,

        @NotBlank(message = "Password is required")
        String password
) {}
