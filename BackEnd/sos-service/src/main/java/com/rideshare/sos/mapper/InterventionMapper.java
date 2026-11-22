package com.rideshare.sos.mapper;

import com.rideshare.sos.dto.request.InterventionRequest;
import com.rideshare.sos.dto.response.InterventionResponse;
import com.rideshare.sos.entity.Intervention;

import java.util.ArrayList;
import java.util.List;

public final class InterventionMapper {

    private InterventionMapper() {
    }

    public static Intervention toEntity(InterventionRequest r) {
        Intervention i = new Intervention();
        updateEntity(i, r);
        return i;
    }

    public static void updateEntity(Intervention i, InterventionRequest r) {
        i.setIdDemande(r.idDemande());
        i.setIdMecanicien(r.idMecanicien());
        i.setDiagnostic(r.diagnostic());
        i.getPiecesRemplacees().clear();
        if (r.piecesRemplacees() != null) {
            i.getPiecesRemplacees().addAll(r.piecesRemplacees());
        }
        i.setDureeMinutes(r.dureeMinutes());
        i.setCoutFinal(r.coutFinal());
        i.setDateDebut(r.dateDebut());
        i.setDateFin(r.dateFin());
    }

    public static InterventionResponse toResponse(Intervention i) {
        return new InterventionResponse(
                i.getId(), i.getIdDemande(), i.getIdMecanicien(), i.getDiagnostic(),
                new ArrayList<>(i.getPiecesRemplacees()),
                i.getDureeMinutes(), i.getCoutFinal(), i.getDateDebut(), i.getDateFin());
    }
}