package com.rideshare.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record RechargeRequest(

        @NotNull(message = "driverId est obligatoire")
        UUID driverId,

        @NotNull(message = "montant est obligatoire")
        @DecimalMin(value = "0.01", message = "le montant de la recharge doit être strictement positif")
        BigDecimal montant
) {
}
