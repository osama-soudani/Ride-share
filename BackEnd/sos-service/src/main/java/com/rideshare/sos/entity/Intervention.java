package com.rideshare.sos.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "intervention",
        uniqueConstraints = @UniqueConstraint(name = "uk_intervention_demande", columnNames = "id_demande"),
        indexes = @Index(name = "idx_intervention_mecanicien", columnList = "id_mecanicien"))
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Plain id of the DemandeSOS. UNIQUE: one intervention per request. */
    @Column(name = "id_demande", nullable = false)
    private Long idDemande;

    /** Plain id of the Mecanicien who did the repair. */
    @Column(name = "id_mecanicien", nullable = false)
    private Long idMecanicien;

    @Column(nullable = false, length = 2000)
    private String diagnostic;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "intervention_pieces",
            joinColumns = @JoinColumn(name = "intervention_id"))
    @Column(name = "piece", nullable = false, length = 100)
    private List<String> piecesRemplacees = new ArrayList<>();

    @Column(name = "duree_minutes", nullable = false)
    private Integer dureeMinutes;

    @Column(name = "cout_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal coutFinal;

    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @Column(name = "date_fin", nullable = false)
    private Instant dateFin;

    public Intervention() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdDemande() { return idDemande; }
    public void setIdDemande(Long idDemande) { this.idDemande = idDemande; }

    public Long getIdMecanicien() { return idMecanicien; }
    public void setIdMecanicien(Long idMecanicien) { this.idMecanicien = idMecanicien; }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    public List<String> getPiecesRemplacees() { return piecesRemplacees; }
    public void setPiecesRemplacees(List<String> piecesRemplacees) { this.piecesRemplacees = piecesRemplacees; }

    public Integer getDureeMinutes() { return dureeMinutes; }
    public void setDureeMinutes(Integer dureeMinutes) { this.dureeMinutes = dureeMinutes; }

    public BigDecimal getCoutFinal() { return coutFinal; }
    public void setCoutFinal(BigDecimal coutFinal) { this.coutFinal = coutFinal; }

    public Instant getDateDebut() { return dateDebut; }
    public void setDateDebut(Instant dateDebut) { this.dateDebut = dateDebut; }

    public Instant getDateFin() { return dateFin; }
    public void setDateFin(Instant dateFin) { this.dateFin = dateFin; }
}