package com.rideshare.payment.dto;

import com.rideshare.payment.entity.TypeTransaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        TypeTransaction type,
        BigDecimal montant,
        UUID courseId,
        Instant date
) {
}
