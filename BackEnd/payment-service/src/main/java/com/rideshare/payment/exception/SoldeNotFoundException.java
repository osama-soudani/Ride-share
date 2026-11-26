package com.rideshare.payment.exception;

import java.util.UUID;

public class SoldeNotFoundException extends RuntimeException {
    public SoldeNotFoundException(UUID idChauffeur) {
        super("Aucun solde trouvé pour le chauffeur : " + idChauffeur);
    }
}
