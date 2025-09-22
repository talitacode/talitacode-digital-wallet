package com.talitacode.digitalwallet.service;


import com.talitacode.digitalwallet.dto.AuthRequest;
import com.talitacode.digitalwallet.dto.UserRegisterRequest;
import com.talitacode.digitalwallet.entity.User;
import com.talitacode.digitalwallet.repository.UserRepository;
import com.talitacode.digitalwallet.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AuthServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtUtil = Mockito.mock(JwtUtil.class);

        authService = new AuthService(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void shouldRegisterNewUser() {
        UserRegisterRequest request = new UserRegisterRequest("Talita Code", "12345678901", "talita", "123456");

        Mockito.when(userRepository.existsByCpf(request.cpf())).thenReturn(false);
        Mockito.when(userRepository.findByLogin(request.login())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        String result = authService.register(request);

        assertEquals("User registered successfully", result);
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void shouldFailLoginWithInvalidPassword() {
        AuthRequest request = new AuthRequest("talita", "wrongPass");

        User user = User.builder().login("talita").passwordHash("encodedPass").build();

        Mockito.when(userRepository.findByLogin("talita")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(request.password(), user.getPasswordHash())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }
}

