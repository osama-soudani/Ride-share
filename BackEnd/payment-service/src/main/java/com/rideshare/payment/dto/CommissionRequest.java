package com.rideshare.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Requête envoyée par un autre microservice (ex. trip-service) à la clôture
 * d'une course, pour déclencher le prélèvement de la commission.
 */
public record CommissionRequest(

        @NotNull(message = "driverId est obligatoire")
        UUID driverId,

        @NotNull(message = "courseId est obligatoire")
        UUID courseId,

        @NotNull(message = "montantCourse est obligatoire")
        @DecimalMin(value = "0.01", message = "le montant de la course doit être strictement positif")
        BigDecimal montantCourse
) {
}
