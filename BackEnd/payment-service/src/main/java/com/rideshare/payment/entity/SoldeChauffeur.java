package com.rideshare.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Représente le solde prépayé d'un chauffeur.
 * Le chauffeur recharge ce solde manuellement ; la plateforme y prélève
 * automatiquement sa commission à la clôture de chaque course.
 */
@Entity
@Table(name = "solde_chauffeur", uniqueConstraints = @UniqueConstraint(columnNames = "id_chauffeur"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoldeChauffeur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Identifiant du chauffeur côté Keycloak (claim "sub" du JWT).
     * Référence logique uniquement : aucune clé étrangère physique vers
     * une autre base de données, conformément au principe "Database per Service".
     */
    @Column(name = "id_chauffeur", nullable = false, updatable = false)
    private UUID idChauffeur;

    @Column(name = "montant", nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_maj", nullable = false)
    private Instant dateMaj;

    @Version
    private Long version; // verrouillage optimiste, utile en cas d'accès concurrent au solde

    @OneToMany(mappedBy = "solde", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public static SoldeChauffeur nouveauSolde(UUID idChauffeur) {
        return SoldeChauffeur.builder()
                .idChauffeur(idChauffeur)
                .montant(BigDecimal.ZERO)
                .dateMaj(Instant.now())
                .build();
    }
}
