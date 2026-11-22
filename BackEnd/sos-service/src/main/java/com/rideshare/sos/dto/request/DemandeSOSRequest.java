package com.rideshare.sos.dto.request;

import com.rideshare.sos.dto.GeoPointDto;
import com.rideshare.sos.entity.enums.NiveauUrgence;
import com.rideshare.sos.entity.enums.TypePanne;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Data used to create an SOS request. The status starts at EN_ATTENTE.")
public record DemandeSOSRequest(

        @Schema(description = "Driver id (user-service)", example = "665f1c2e8b3a4d0012ab34ce")
        @NotBlank(message = "idChauffeur is required")
        @Size(max = 24, message = "idChauffeur must be at most 24 characters")
        String idChauffeur,

        @Schema(description = "RideShare journey id", example = "665f1c2e8b3a4d0012ab34cf")
        @NotBlank(message = "idCourseRef is required")
        @Size(max = 24, message = "idCourseRef must be at most 24 characters")
        String idCourseRef,

        @NotNull(message = "typePanne is required")
        TypePanne typePanne,

        @NotNull(message = "niveauUrgence is required")
        NiveauUrgence niveauUrgence,

        @NotNull(message = "position is required")
        @Valid
        GeoPointDto position
) {
}