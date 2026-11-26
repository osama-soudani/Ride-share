package com.rideshare.payment.exception;

/**
 * Levée lorsqu'un chauffeur authentifié tente de consulter le solde ou
 * l'historique d'un AUTRE chauffeur, sans être ADMIN.
 */
public class AccessDeniedCustomException extends RuntimeException {
    public AccessDeniedCustomException(String message) {
        super(message);
    }
}
