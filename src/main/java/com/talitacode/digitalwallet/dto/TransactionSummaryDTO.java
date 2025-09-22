package com.talitacode.digitalwallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.talitacode.digitalwallet.entity.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionSummaryDTO(
        TransactionType type,
        BigDecimal value,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime date
) implements Serializable {}
