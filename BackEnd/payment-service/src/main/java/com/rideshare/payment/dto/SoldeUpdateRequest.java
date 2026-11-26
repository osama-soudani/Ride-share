package com.rideshare.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SoldeUpdateRequest(
        @NotNull(message = "montant est obligatoire")
        @DecimalMin(value = "0.00", message = "le montant ne peut pas être négatif")
        BigDecimal montant
) {
}
