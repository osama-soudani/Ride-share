package com.rideshare.sos.dto.request;

import com.rideshare.sos.dto.GeoPointDto;
import com.rideshare.sos.entity.enums.NiveauUrgence;
import com.rideshare.sos.entity.enums.StatutDemandeSOS;
import com.rideshare.sos.entity.enums.TypePanne;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Full replacement of the editable fields of an SOS request")
public record DemandeSOSUpdateRequest(

        @NotNull(message = "typePanne is required")
        TypePanne typePanne,

        @NotNull(message = "niveauUrgence is required")
        NiveauUrgence niveauUrgence,

        @NotNull(message = "position is required")
        @Valid
        GeoPointDto position,

        @NotNull(message = "statut is required")
        StatutDemandeSOS statut,

        @Schema(description = "Assigned mechanic id, or null if none", nullable = true)
        @Positive(message = "idMecanicien must be positive")
        Long idMecanicien
) {
}