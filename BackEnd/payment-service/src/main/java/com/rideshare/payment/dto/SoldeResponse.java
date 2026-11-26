package com.rideshare.payment.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SoldeResponse(
        UUID driverId,
        BigDecimal montant,
        Instant dateMaj
) {
}
