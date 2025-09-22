package com.talitacode.digitalwallet.service;

import com.talitacode.digitalwallet.entity.Account;
import com.talitacode.digitalwallet.repository.AccountRepository;
import com.talitacode.digitalwallet.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOps;

    @InjectMocks
    private AccountService accountService;

    private UUID accountId;
    private Account account;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        accountId = UUID.randomUUID();
        account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.ZERO);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
    }

    @Test
    void shouldDepositSuccessfully() {
        accountService.deposit(accountId, new BigDecimal("100.00"));

        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any());
        verify(valueOps, times(1)).set("account:balance:" + accountId, new BigDecimal("100.00"));
    }
}