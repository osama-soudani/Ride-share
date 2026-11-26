package com.rideshare.payment.service.impl;

import com.rideshare.payment.dto.CommissionRequest;
import com.rideshare.payment.dto.RechargeRequest;
import com.rideshare.payment.dto.SoldeCreateRequest;
import com.rideshare.payment.dto.SoldeResponse;
import com.rideshare.payment.dto.SoldeUpdateRequest;
import com.rideshare.payment.dto.TransactionResponse;
import com.rideshare.payment.entity.SoldeChauffeur;
import com.rideshare.payment.entity.Transaction;
import com.rideshare.payment.entity.TypeTransaction;
import com.rideshare.payment.exception.InsufficientBalanceException;
import com.rideshare.payment.exception.SoldeAlreadyExistsException;
import com.rideshare.payment.exception.SoldeNotFoundException;
import com.rideshare.payment.repository.SoldeChauffeurRepository;
import com.rideshare.payment.repository.TransactionRepository;
import com.rideshare.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final SoldeChauffeurRepository soldeRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Taux de commission appliqué par la plateforme, externalisé dans
     * application.properties (ex: commission.rate=0.10 pour 10%).
     */
    @Value("${commission.rate}")
    private BigDecimal commissionRate;

    @Override
    @Transactional(readOnly = true)
    public SoldeResponse consulterSolde(UUID driverId) {
        SoldeChauffeur solde = getOuCreerSolde(driverId);
        return toSoldeResponse(solde);
    }

    @Override
    @Transactional
    public TransactionResponse recharger(RechargeRequest request) {
        SoldeChauffeur solde = getOuCreerSoldeForUpdate(request.driverId());

        solde.setMontant(solde.getMontant().add(request.montant()));
        solde.setDateMaj(Instant.now());
        soldeRepository.save(solde);

        Transaction transaction = Transaction.builder()
                .solde(solde)
                .type(TypeTransaction.RECHARGE)
                .montant(request.montant())
                .idCourseRef(null)
                .date(Instant.now())
                .build();
        transactionRepository.save(transaction);

        return toTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse preleverCommission(CommissionRequest request) {
        // Verrou pessimiste : empêche deux prélèvements concurrents sur le même
        // chauffeur de lire un solde périmé et de le débiter deux fois à tort.
        SoldeChauffeur solde = getOuCreerSoldeForUpdate(request.driverId());

        BigDecimal montantCommission = request.montantCourse()
                .multiply(commissionRate)
                .setScale(2, RoundingMode.HALF_UP);

        if (solde.getMontant().compareTo(montantCommission) < 0) {
            // Solde insuffisant : on NE modifie rien et on remonte l'erreur,
            // traduite en HTTP 402 par le GlobalExceptionHandler.
            throw new InsufficientBalanceException(request.driverId(), solde.getMontant(), montantCommission);
        }

        solde.setMontant(solde.getMontant().subtract(montantCommission));
        solde.setDateMaj(Instant.now());
        soldeRepository.save(solde);

        Transaction transaction = Transaction.builder()
                .solde(solde)
                .type(TypeTransaction.COMMISSION)
                .montant(montantCommission)
                .idCourseRef(request.courseId())
                .date(Instant.now())
                .build();
        transactionRepository.save(transaction);

        return toTransactionResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> historique(UUID driverId, Pageable pageable) {
        return transactionRepository.findBySolde_IdChauffeurOrderByDateDesc(driverId, pageable)
                .map(this::toTransactionResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SoldeResponse> listerSoldes(Pageable pageable) {
        return soldeRepository.findAll(pageable).map(this::toSoldeResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SoldeResponse consulterSoldeParId(UUID soldeId) {
        SoldeChauffeur solde = soldeRepository.findById(soldeId)
                .orElseThrow(() -> new SoldeNotFoundException(soldeId));
        return toSoldeResponse(solde);
    }

    @Override
    @Transactional
    public SoldeResponse creerSolde(SoldeCreateRequest request) {
        if (soldeRepository.findByIdChauffeur(request.driverId()).isPresent()) {
            throw new SoldeAlreadyExistsException(request.driverId());
        }

        SoldeChauffeur solde = SoldeChauffeur.builder()
                .idChauffeur(request.driverId())
                .montant(request.montant())
                .dateMaj(Instant.now())
                .build();
        return toSoldeResponse(soldeRepository.save(solde));
    }

    @Override
    @Transactional
    public SoldeResponse modifierSolde(UUID soldeId, SoldeUpdateRequest request) {
        SoldeChauffeur solde = soldeRepository.findById(soldeId)
                .orElseThrow(() -> new SoldeNotFoundException(soldeId));
        solde.setMontant(request.montant());
        solde.setDateMaj(Instant.now());
        return toSoldeResponse(soldeRepository.save(solde));
    }

    @Override
    @Transactional
    public void supprimerSolde(UUID soldeId) {
        SoldeChauffeur solde = soldeRepository.findById(soldeId)
                .orElseThrow(() -> new SoldeNotFoundException(soldeId));
        soldeRepository.delete(solde);
    }

    // ----------------- Méthodes utilitaires privées -----------------

    private SoldeChauffeur getOuCreerSolde(UUID idChauffeur) {
        return soldeRepository.findByIdChauffeur(idChauffeur)
                .orElseGet(() -> soldeRepository.save(SoldeChauffeur.nouveauSolde(idChauffeur)));
    }

    private SoldeChauffeur getOuCreerSoldeForUpdate(UUID idChauffeur) {
        return soldeRepository.findByIdChauffeurForUpdate(idChauffeur)
                .orElseGet(() -> soldeRepository.save(SoldeChauffeur.nouveauSolde(idChauffeur)));
    }

    private SoldeResponse toSoldeResponse(SoldeChauffeur solde) {
        return new SoldeResponse(solde.getIdChauffeur(), solde.getMontant(), solde.getDateMaj());
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getMontant(),
                transaction.getIdCourseRef(),
                transaction.getDate()
        );
    }
}
