package com.talitacode.digitalwallet.controller;

import com.talitacode.digitalwallet.dto.DepositRequest;
import com.talitacode.digitalwallet.dto.PaymentRequest;
import com.talitacode.digitalwallet.dto.AccountSummary;
import com.talitacode.digitalwallet.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/{accountId}/transactions/deposit")
    @ResponseStatus(HttpStatus.OK)
    public AccountSummary deposit(
            @PathVariable UUID accountId,
            @Valid @RequestBody DepositRequest request) {

        log.info("HTTP POST /accounts/{}/transactions/deposit, body={}", accountId, request);
        accountService.deposit(accountId, request.amount());
        return accountService.getSummary(accountId);
    }


    @PostMapping("/{accountId}/transactions/pay")
    public ResponseEntity<AccountSummary> pay(
            @PathVariable UUID accountId,
            @Valid @RequestBody PaymentRequest request) {

        log.info("HTTP POST /accounts/{}/transactions/pay, body={}", accountId, request);
        accountService.pay(accountId, request.amount());
        return ResponseEntity.ok(accountService.getSummary(accountId));
    }


    @GetMapping("/{accountId}/summary")
    public ResponseEntity<AccountSummary> getSummary(@PathVariable UUID accountId) {
        log.info("HTTP GET /accounts/{}/summary", accountId);
        return ResponseEntity.ok(accountService.getSummary(accountId));
    }
}
