package com.rideshare.sos.mapper;

import com.rideshare.sos.common.geo.GeometryUtils;
import com.rideshare.sos.dto.request.MecanicienRequest;
import com.rideshare.sos.dto.response.MecanicienResponse;
import com.rideshare.sos.entity.Mecanicien;

import java.util.HashSet;

public final class MecanicienMapper {

    private MecanicienMapper() {
    }

    public static Mecanicien toEntity(MecanicienRequest r) {
        Mecanicien m = new Mecanicien();
        updateEntity(m, r);
        return m;
    }

    /** Full replacement (PUT semantics). */
    public static void updateEntity(Mecanicien m, MecanicienRequest r) {
        m.setIdCompte(r.idCompte());
        m.setNom(r.nom());
        m.setTelephone(r.telephone());
        m.getSpecialites().clear();
        m.getSpecialites().addAll(new HashSet<>(r.specialites()));
        m.setPosition(GeometryUtils.toPoint(r.position()));
        m.setRayonInterventionKm(r.rayonInterventionKm());
        m.setDisponible(r.disponible());
        m.setStatut(r.statut());
        m.setNoteMoyenne(r.noteMoyenne());
    }

    public static MecanicienResponse toResponse(Mecanicien m) {
        return new MecanicienResponse(
                m.getId(), m.getIdCompte(), m.getNom(), m.getTelephone(),
                new HashSet<>(m.getSpecialites()),
                GeometryUtils.toDto(m.getPosition()),
                m.getRayonInterventionKm(), m.isDisponible(), m.getStatut(),
                m.getNoteMoyenne(), m.getDateCreation());
    }
}