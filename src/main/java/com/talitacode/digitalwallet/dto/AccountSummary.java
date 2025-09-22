package com.talitacode.digitalwallet.dto;

import java.math.BigDecimal;
import java.util.List;

public record AccountSummary(
        BigDecimal saldoTotal,
        List<TransactionSummaryDTO> historico
) {}
