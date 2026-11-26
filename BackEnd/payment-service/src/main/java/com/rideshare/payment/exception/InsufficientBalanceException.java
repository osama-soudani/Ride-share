package com.rideshare.payment.exception;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Levée lorsqu'un prélèvement de commission est demandé alors que le solde
 * du chauffeur est insuffisant. Traduite en HTTP 402 Payment Required.
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(UUID idChauffeur, BigDecimal soldeActuel, BigDecimal montantRequis) {
        super(String.format(
                "Solde insuffisant pour le chauffeur %s : solde actuel = %s, montant requis = %s",
                idChauffeur, soldeActuel, montantRequis
        ));
    }
}
