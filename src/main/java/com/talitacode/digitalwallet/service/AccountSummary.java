package com.talitacode.digitalwallet.service;

import com.talitacode.digitalwallet.dto.TransactionSummaryDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public record AccountSummary(
        BigDecimal saldoTotal,
        List<TransactionSummaryDTO> historico
) implements Serializable {}
