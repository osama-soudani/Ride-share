package com.rideshare.payment.exception;

import java.util.UUID;

public class SoldeAlreadyExistsException extends RuntimeException {
    public SoldeAlreadyExistsException(UUID idChauffeur) {
        super("Un solde existe déjà pour le chauffeur : " + idChauffeur);
    }
}
