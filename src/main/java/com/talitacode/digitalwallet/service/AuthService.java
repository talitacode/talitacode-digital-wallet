package com.talitacode.digitalwallet.service;

import com.talitacode.digitalwallet.dto.AuthRequest;
import com.talitacode.digitalwallet.dto.AuthResponse;
import com.talitacode.digitalwallet.dto.UserRegisterRequest;
import com.talitacode.digitalwallet.entity.Account;
import com.talitacode.digitalwallet.entity.User;
import com.talitacode.digitalwallet.repository.UserRepository;
import com.talitacode.digitalwallet.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(UserRegisterRequest request) {
        if (userRepository.existsByCpf(request.cpf()) || userRepository.findByLogin(request.login()).isPresent()) {
            log.warn("User with login={} or cpf={} already exists", request.login(), request.cpf());
            throw new IllegalArgumentException("User already exists");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .cpf(request.cpf())
                .login(request.login())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        user.setAccount(Account.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build());

        userRepository.save(user);

        return "User registered successfully";
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> {
                    log.error("Login failed: user {} not found", request.login());
                    return new IllegalArgumentException("Invalid credentials");
                });

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.error("Login failed: user {} not found", request.login());
            throw new IllegalArgumentException("Invalid credentials");
        }

        return new AuthResponse(
                jwtUtil.generateToken(user.getLogin()),
                user.getAccount().getId()
        );
    }
}

