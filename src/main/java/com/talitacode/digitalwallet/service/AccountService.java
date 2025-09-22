package com.talitacode.digitalwallet.service;

import com.talitacode.digitalwallet.dto.AccountSummary;
import com.talitacode.digitalwallet.dto.TransactionSummaryDTO;
import com.talitacode.digitalwallet.entity.Account;
import com.talitacode.digitalwallet.entity.Transaction;
import com.talitacode.digitalwallet.entity.TransactionType;
import com.talitacode.digitalwallet.repository.AccountRepository;
import com.talitacode.digitalwallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private static final BigDecimal INTEREST_RATE = new BigDecimal("1.02");

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void deposit(UUID accountId, BigDecimal amount) {
        log.info("Service: Deposit request -> accountId={}, amount={}", accountId, amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Deposit failed: accountId {} not found", accountId);
                    return new IllegalArgumentException("Account not found");
                });

        BigDecimal newBalance = account.getBalance();

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal debtWithInterest = newBalance.abs().multiply(INTEREST_RATE);

            if (amount.compareTo(debtWithInterest) >= 0) {
                amount = amount.subtract(debtWithInterest);
                newBalance = amount;
            } else {
                newBalance = newBalance.add(amount); // ainda negativo
                amount = BigDecimal.ZERO;
            }
        } else {
            newBalance = newBalance.add(amount);
        }

        account.setBalance(newBalance);
        accountRepository.save(account);

        transactionRepository.save(Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .build());

        updateCache(account);

        log.info("Service: Deposit successful -> accountId={}, newBalance={}", accountId, newBalance);
    }

    public void pay(UUID accountId, BigDecimal amount) {
        log.info("Service: Payment request -> accountId={}, amount={}", accountId, amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Payment failed: accountId {} not found", accountId);
                    return new IllegalArgumentException("Account not found");
                });

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        transactionRepository.save(Transaction.builder()
                .account(account)
                .type(TransactionType.PAYMENT)
                .amount(amount.negate())
                .build());

        updateCache(account);

        log.info("Service: Payment processed -> accountId={}, newBalance={}", accountId, newBalance);
    }

    public AccountSummary getSummary(UUID accountId) {
        log.info("Service: Balance summary request -> accountId={}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Summary failed: accountId {} not found", accountId);
                    return new IllegalArgumentException("Account not found");
                });

        List<TransactionSummaryDTO> transactions = transactionRepository.findByAccountId(accountId)
                .stream()
                .map(t -> new TransactionSummaryDTO(
                        t.getType(),
                        t.getAmount(),
                        t.getCreatedAt()
                ))
                .toList();

        AccountSummary summary = new AccountSummary(account.getBalance(), transactions);

        redisTemplate.opsForValue().set("account:summary:" + accountId, summary);

        log.info("Service: Summary generated -> accountId={}, saldoTotal={}, historicoSize={}",
                accountId, summary.saldoTotal(), summary.historico().size());

        return summary;
    }

    private void updateCache(Account account) {
        redisTemplate.opsForValue().set("account:balance:" + account.getId(), account.getBalance());
        log.debug("Cache updated: account:balance:{} = {}", account.getId(), account.getBalance());
    }
}
