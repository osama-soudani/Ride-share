package com.rideshare.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Trace une opération financière sur le solde d'un chauffeur :
 * soit une recharge, soit un prélèvement de commission lié à une course.
 */
@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solde", nullable = false)
    private SoldeChauffeur solde;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TypeTransaction type;

    @Column(name = "montant", nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    /**
     * Référence à la course concernée. Rempli uniquement pour une transaction
     * de type COMMISSION ; nul pour une RECHARGE.
     */
    @Column(name = "id_course_ref")
    private UUID idCourseRef;

    @Column(name = "date_transaction", nullable = false)
    private Instant date;
}
