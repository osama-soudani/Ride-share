package com.rideshare.sos.dto.response;

import com.rideshare.sos.dto.GeoPointDto;
import com.rideshare.sos.entity.enums.NiveauUrgence;
import com.rideshare.sos.entity.enums.StatutDemandeSOS;
import com.rideshare.sos.entity.enums.TypePanne;

import java.time.Instant;

public record DemandeSOSResponse(
        Long id,
        String idChauffeur,
        String idCourseRef,
        TypePanne typePanne,
        NiveauUrgence niveauUrgence,
        GeoPointDto position,
        StatutDemandeSOS statut,
        Long idMecanicien,
        Instant dateCreation,
        Instant dateAssignation,
        Instant dateCloture
) {
}