package com.rideshare.sos.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record InterventionResponse(
        Long id,
        Long idDemande,
        Long idMecanicien,
        String diagnostic,
        List<String> piecesRemplacees,
        Integer dureeMinutes,
        BigDecimal coutFinal,
        Instant dateDebut,
        Instant dateFin
) {
}