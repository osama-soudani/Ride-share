package com.rideshare.payment.service;

import com.rideshare.payment.dto.CommissionRequest;
import com.rideshare.payment.dto.RechargeRequest;
import com.rideshare.payment.dto.TransactionResponse;
import com.rideshare.payment.entity.SoldeChauffeur;
import com.rideshare.payment.entity.Transaction;
import com.rideshare.payment.exception.InsufficientBalanceException;
import com.rideshare.payment.repository.SoldeChauffeurRepository;
import com.rideshare.payment.repository.TransactionRepository;
import com.rideshare.payment.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private SoldeChauffeurRepository soldeRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private PaymentServiceImpl paymentService;

    private final UUID driverId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentServiceImpl(soldeRepository, transactionRepository);
        // Taux de commission fixé à 10% pour les tests (équivaut à la valeur
        // injectée depuis application.properties en conditions réelles).
        ReflectionTestUtils.setField(paymentService, "commissionRate", new BigDecimal("0.10"));
    }

    @Test
    void preleverCommission_doitDebiterLeSoldeQuandIlEstSuffisant() {
        SoldeChauffeur solde = SoldeChauffeur.builder()
                .id(UUID.randomUUID())
                .idChauffeur(driverId)
                .montant(new BigDecimal("50.00"))
                .dateMaj(Instant.now())
                .build();

        when(soldeRepository.findByIdChauffeurForUpdate(driverId)).thenReturn(Optional.of(solde));
        when(soldeRepository.save(any(SoldeChauffeur.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        CommissionRequest request = new CommissionRequest(driverId, UUID.randomUUID(), new BigDecimal("20.00"));

        TransactionResponse response = paymentService.preleverCommission(request);

        // Commission = 10% de 20.00 = 2.00 ; solde attendu = 50.00 - 2.00 = 48.00
        assertThat(response.montant()).isEqualByComparingTo("2.00");
        assertThat(solde.getMontant()).isEqualByComparingTo("48.00");

        ArgumentCaptor<SoldeChauffeur> captor = ArgumentCaptor.forClass(SoldeChauffeur.class);
        verify(soldeRepository).save(captor.capture());
        assertThat(captor.getValue().getMontant()).isEqualByComparingTo("48.00");
    }

    @Test
    void preleverCommission_doitRefuserQuandLeSoldeEstInsuffisant() {
        SoldeChauffeur solde = SoldeChauffeur.builder()
                .id(UUID.randomUUID())
                .idChauffeur(driverId)
                .montant(new BigDecimal("1.00")) // trop faible pour couvrir la commission
                .dateMaj(Instant.now())
                .build();

        when(soldeRepository.findByIdChauffeurForUpdate(driverId)).thenReturn(Optional.of(solde));

        CommissionRequest request = new CommissionRequest(driverId, UUID.randomUUID(), new BigDecimal("50.00"));

        assertThatThrownBy(() -> paymentService.preleverCommission(request))
                .isInstanceOf(InsufficientBalanceException.class);

        // Le solde ne doit SURTOUT PAS avoir été modifié ni sauvegardé.
        assertThat(solde.getMontant()).isEqualByComparingTo("1.00");
        verify(soldeRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recharger_doitAugmenterLeSoldeEtEnregistrerUneTransaction() {
        SoldeChauffeur solde = SoldeChauffeur.builder()
                .id(UUID.randomUUID())
                .idChauffeur(driverId)
                .montant(new BigDecimal("10.00"))
                .dateMaj(Instant.now())
                .build();

        when(soldeRepository.findByIdChauffeurForUpdate(driverId)).thenReturn(Optional.of(solde));
        when(soldeRepository.save(any(SoldeChauffeur.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        RechargeRequest request = new RechargeRequest(driverId, new BigDecimal("25.00"));

        TransactionResponse response = paymentService.recharger(request);

        assertThat(response.montant()).isEqualByComparingTo("25.00");
        assertThat(solde.getMontant()).isEqualByComparingTo("35.00");
    }

    @Test
    void preleverCommission_doitCreerUnSoldeAZeroSiInexistant_puisEchouer() {
        when(soldeRepository.findByIdChauffeurForUpdate(driverId)).thenReturn(Optional.empty());
        when(soldeRepository.save(any(SoldeChauffeur.class))).thenAnswer(inv -> inv.getArgument(0));

        CommissionRequest request = new CommissionRequest(driverId, UUID.randomUUID(), new BigDecimal("10.00"));

        assertThatThrownBy(() -> paymentService.preleverCommission(request))
                .isInstanceOf(InsufficientBalanceException.class);
    }
}
