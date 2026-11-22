package com.rideshare.sos.dto.request;

import com.rideshare.sos.dto.GeoPointDto;
import com.rideshare.sos.entity.enums.Specialite;
import com.rideshare.sos.entity.enums.StatutMecanicien;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Set;

@Schema(description = "Data used to create or fully replace a mechanic")
public record MecanicienRequest(

        @Schema(description = "auth-service account id", example = "665f1c2e8b3a4d0012ab34cd")
        @Size(max = 24, message = "idCompte must be at most 24 characters")
        String idCompte,

        @Schema(example = "Karim Mansour")
        @NotBlank(message = "nom is required")
        @Size(max = 100, message = "nom must be at most 100 characters")
        String nom,

        @Schema(example = "+21620123456")
        @NotBlank(message = "telephone is required")
        @Size(max = 20, message = "telephone must be at most 20 characters")
        String telephone,

        @NotEmpty(message = "at least one specialite is required")
        Set<Specialite> specialites,

        @NotNull(message = "position is required")
        @Valid
        GeoPointDto position,

        @Schema(description = "Maximum intervention radius in km", example = "25")
        @NotNull(message = "rayonInterventionKm is required")
        @Positive(message = "rayonInterventionKm must be positive")
        Double rayonInterventionKm,

        @NotNull(message = "disponible is required")
        Boolean disponible,

        @NotNull(message = "statut is required")
        StatutMecanicien statut,

        @Schema(description = "Average rating (0 to 5), optional", example = "4.5")
        @DecimalMin(value = "0.0", message = "noteMoyenne must be >= 0")
        @DecimalMax(value = "5.0", message = "noteMoyenne must be <= 5")
        Double noteMoyenne
) {
}