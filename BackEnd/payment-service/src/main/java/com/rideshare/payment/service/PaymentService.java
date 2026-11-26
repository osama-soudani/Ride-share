package com.rideshare.payment.service;

import com.rideshare.payment.dto.CommissionRequest;
import com.rideshare.payment.dto.RechargeRequest;
import com.rideshare.payment.dto.SoldeCreateRequest;
import com.rideshare.payment.dto.SoldeResponse;
import com.rideshare.payment.dto.SoldeUpdateRequest;
import com.rideshare.payment.dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {

    /**
     * Retourne le solde d'un chauffeur. Crée un solde à zéro s'il n'en existe
     * pas encore (premier appel pour ce chauffeur).
     */
    SoldeResponse consulterSolde(UUID driverId);

    /**
     * Enregistre une recharge et augmente le solde du chauffeur.
     */
    TransactionResponse recharger(RechargeRequest request);

    /**
     * Prélève la commission liée à une course terminée.
     * Lève InsufficientBalanceException si le solde est insuffisant (aucune
     * modification n'est alors effectuée).
     */
    TransactionResponse preleverCommission(CommissionRequest request);

    Page<TransactionResponse> historique(UUID driverId, Pageable pageable);

    Page<SoldeResponse> listerSoldes(Pageable pageable);

    SoldeResponse consulterSoldeParId(UUID soldeId);

    SoldeResponse creerSolde(SoldeCreateRequest request);

    SoldeResponse modifierSolde(UUID soldeId, SoldeUpdateRequest request);

    void supprimerSolde(UUID soldeId);
}
