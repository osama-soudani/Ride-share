package com.rideshare.sos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Schema(description = "Data used to create or fully replace an intervention")
public record InterventionRequest(

        @NotNull(message = "idDemande is required")
        @Positive(message = "idDemande must be positive")
        Long idDemande,

        @NotNull(message = "idMecanicien is required")
        @Positive(message = "idMecanicien must be positive")
        Long idMecanicien,

        @Schema(example = "Alternator belt broken")
        @NotBlank(message = "diagnostic is required")
        @Size(max = 2000, message = "diagnostic must be at most 2000 characters")
        String diagnostic,

        @Schema(description = "Replaced parts (may be empty)", example = "[\"Alternator belt\"]")
        List<@NotBlank(message = "a part name cannot be blank") @Size(max = 100) String> piecesRemplacees,

        @Schema(example = "45")
        @NotNull(message = "dureeMinutes is required")
        @Positive(message = "dureeMinutes must be positive")
        Integer dureeMinutes,

        @Schema(example = "120.50")
        @NotNull(message = "coutFinal is required")
        @DecimalMin(value = "0.0", message = "coutFinal cannot be negative")
        @Digits(integer = 8, fraction = 2, message = "coutFinal: max 8 digits and 2 decimals")
        BigDecimal coutFinal,

        @Schema(example = "2026-11-21T10:00:00Z")
        @NotNull(message = "dateDebut is required")
        Instant dateDebut,

        @Schema(example = "2026-11-21T10:45:00Z")
        @NotNull(message = "dateFin is required")
        Instant dateFin
) {

    /** Cross-field rule: the end cannot precede the start. */
    @Schema(hidden = true)
    @AssertTrue(message = "dateFin must not be before dateDebut")
    public boolean isDateRangeValid() {
        return dateDebut == null || dateFin == null || !dateFin.isBefore(dateDebut);
    }
}