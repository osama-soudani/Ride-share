package com.rideshare.sos.mapper;

import com.rideshare.sos.common.geo.GeometryUtils;
import com.rideshare.sos.dto.request.DemandeSOSRequest;
import com.rideshare.sos.dto.request.DemandeSOSUpdateRequest;
import com.rideshare.sos.dto.response.DemandeSOSResponse;
import com.rideshare.sos.entity.DemandeSOS;
import com.rideshare.sos.entity.enums.StatutDemandeSOS;

public final class DemandeSOSMapper {

    private DemandeSOSMapper() {
    }

    public static DemandeSOS toEntity(DemandeSOSRequest r) {
        DemandeSOS d = new DemandeSOS();
        d.setIdChauffeur(r.idChauffeur());
        d.setIdCourseRef(r.idCourseRef());
        d.setTypePanne(r.typePanne());
        d.setNiveauUrgence(r.niveauUrgence());
        d.setPosition(GeometryUtils.toPoint(r.position()));
        d.setStatut(StatutDemandeSOS.EN_ATTENTE);
        return d;
    }

    public static void updateEntity(DemandeSOS d, DemandeSOSUpdateRequest r) {
        d.setTypePanne(r.typePanne());
        d.setNiveauUrgence(r.niveauUrgence());
        d.setPosition(GeometryUtils.toPoint(r.position()));
        d.setStatut(r.statut());
        d.setIdMecanicien(r.idMecanicien());
    }

    public static DemandeSOSResponse toResponse(DemandeSOS d) {
        return new DemandeSOSResponse(
                d.getId(), d.getIdChauffeur(), d.getIdCourseRef(),
                d.getTypePanne(), d.getNiveauUrgence(),
                GeometryUtils.toDto(d.getPosition()),
                d.getStatut(), d.getIdMecanicien(),
                d.getDateCreation(), d.getDateAssignation(), d.getDateCloture());
    }
}