package com.rideshare.payment.exception;

import com.rideshare.payment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Centralise la traduction des exceptions métier en réponses HTTP cohérentes,
 * au lieu de laisser chaque contrôleur gérer ses propres try/catch.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoldeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSoldeNotFound(SoldeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(404, "SOLDE_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(SoldeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleSoldeAlreadyExists(SoldeAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(409, "SOLDE_ALREADY_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException ex) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(ErrorResponse.of(402, "INSUFFICIENT_BALANCE", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedCustomException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedCustomException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(403, "ACCESS_DENIED", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " : " + f.getDefaultMessage())
                .collect(Collectors.joining(" ; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(400, "VALIDATION_ERROR", message));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ErrorResponse.of(500, "INTERNAL_ERROR", "Une erreur inattendue est survenue."));
//    }
}
