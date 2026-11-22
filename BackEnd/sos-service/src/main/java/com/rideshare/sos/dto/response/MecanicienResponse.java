package com.rideshare.sos.dto.response;

import com.rideshare.sos.dto.GeoPointDto;
import com.rideshare.sos.entity.enums.Specialite;
import com.rideshare.sos.entity.enums.StatutMecanicien;

import java.time.Instant;
import java.util.Set;

public record MecanicienResponse(
        Long id,
        String idCompte,
        String nom,
        String telephone,
        Set<Specialite> specialites,
        GeoPointDto position,
        Double rayonInterventionKm,
        boolean disponible,
        StatutMecanicien statut,
        Double noteMoyenne,
        Instant dateCreation
) {
}