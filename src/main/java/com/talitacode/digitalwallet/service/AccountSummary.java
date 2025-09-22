package com.talitacode.digitalwallet.service;

import com.talitacode.digitalwallet.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public record AccountSummary(
        BigDecimal saldoTotal,
        List<Transaction> historico
) {}

