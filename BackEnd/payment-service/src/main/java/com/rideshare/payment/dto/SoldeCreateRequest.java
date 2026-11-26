package com.rideshare.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record SoldeCreateRequest(
        @NotNull(message = "driverId est obligatoire")
        UUID driverId,

        @NotNull(message = "montant est obligatoire")
        @DecimalMin(value = "0.00", message = "le montant ne peut pas être négatif")
        BigDecimal montant
) {
}
