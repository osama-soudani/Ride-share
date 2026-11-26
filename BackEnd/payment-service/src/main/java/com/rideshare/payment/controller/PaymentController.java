package com.rideshare.payment.controller;

import com.rideshare.payment.dto.CommissionRequest;
import com.rideshare.payment.dto.RechargeRequest;
import com.rideshare.payment.dto.SoldeCreateRequest;
import com.rideshare.payment.dto.SoldeResponse;
import com.rideshare.payment.dto.SoldeUpdateRequest;
import com.rideshare.payment.dto.TransactionResponse;
import com.rideshare.payment.exception.AccessDeniedCustomException;
import com.rideshare.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/balances")
    public ResponseEntity<Page<SoldeResponse>> listerSoldes(Pageable pageable) {
        return ResponseEntity.ok(paymentService.listerSoldes(pageable));
    }

    @GetMapping("/balances/{soldeId}")
    public ResponseEntity<SoldeResponse> consulterSoldeParId(@PathVariable UUID soldeId) {
        return ResponseEntity.ok(paymentService.consulterSoldeParId(soldeId));
    }

    @PostMapping("/balances")
    public ResponseEntity<SoldeResponse> creerSolde(@Valid @RequestBody SoldeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.creerSolde(request));
    }

    @PutMapping("/balances/{soldeId}")
    public ResponseEntity<SoldeResponse> modifierSolde(
            @PathVariable UUID soldeId,
            @Valid @RequestBody SoldeUpdateRequest request
    ) {
        return ResponseEntity.ok(paymentService.modifierSolde(soldeId, request));
    }

    @DeleteMapping("/balances/{soldeId}")
    public ResponseEntity<Void> supprimerSolde(@PathVariable UUID soldeId) {
        paymentService.supprimerSolde(soldeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/balance/{driverId}")
    public ResponseEntity<SoldeResponse> consulterSolde(@PathVariable UUID driverId,@AuthenticationPrincipal Jwt jwt) {
        verifierAccesPropreCompteOuAdmin(driverId, jwt);
        return ResponseEntity.ok(paymentService.consulterSolde(driverId));
    }

    @PostMapping("/recharge")
    public ResponseEntity<TransactionResponse> recharger(@Valid @RequestBody RechargeRequest request) {
        TransactionResponse response = paymentService.recharger(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/commission")
    @PreAuthorize("hasAuthority('ROLE_SERVICE_INTERNAL') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TransactionResponse> preleverCommission(@Valid @RequestBody CommissionRequest request) {
        TransactionResponse response = paymentService.preleverCommission(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/history/{driverId}")
    public ResponseEntity<List<TransactionResponse>> historique(
            @PathVariable UUID driverId,
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ) {
        verifierAccesPropreCompteOuAdmin(driverId, jwt);
        Page<TransactionResponse> page = paymentService.historique(driverId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // ----------------- Contrôle d'accès -----------------

    private void verifierAccesPropreCompteOuAdmin(UUID driverId, Jwt jwt) {
        if (jwt == null) {
            return; // en dev sans sécurité activée ; ne se produit pas en production
        }
        UUID subjectId = UUID.fromString(jwt.getSubject());
        boolean isAdmin = jwt.getClaimAsStringList("roles") != null
                && jwt.getClaimAsStringList("roles").contains("ADMIN");

        if (!isAdmin && !subjectId.equals(driverId)) {
            throw new AccessDeniedCustomException(
                    "Un chauffeur ne peut consulter que son propre solde/historique.");
        }
    }
}
