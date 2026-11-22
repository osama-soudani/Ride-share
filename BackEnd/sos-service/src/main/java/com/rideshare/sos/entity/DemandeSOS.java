package com.rideshare.sos.entity;

import com.rideshare.sos.entity.enums.NiveauUrgence;
import com.rideshare.sos.entity.enums.StatutDemandeSOS;
import com.rideshare.sos.entity.enums.TypePanne;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Entity
@Table(name = "demande_sos", indexes = {
        @Index(name = "idx_demande_chauffeur", columnList = "id_chauffeur"),
        @Index(name = "idx_demande_mecanicien", columnList = "id_mecanicien"),
        @Index(name = "idx_demande_statut", columnList = "statut")
})
public class DemandeSOS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Logical reference to the driver in user-service (MongoDB ObjectId). */
    @Column(name = "id_chauffeur", nullable = false, length = 24)
    private String idChauffeur;

    /** Logical reference to the RideShare journey (MongoDB ObjectId). */
    @Column(name = "id_course_ref", nullable = false, length = 24)
    private String idCourseRef;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_panne", nullable = false, length = 20)
    private TypePanne typePanne;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_urgence", nullable = false, length = 20)
    private NiveauUrgence niveauUrgence;

    /** Where the vehicle broke down. PostGIS point, SRID 4326. */
    @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutDemandeSOS statut = StatutDemandeSOS.EN_ATTENTE;

    /** Plain id of the assigned Mecanicien (no foreign key). Null until assigned. */
    @Column(name = "id_mecanicien")
    private Long idMecanicien;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private Instant dateCreation;

    @Column(name = "date_assignation")
    private Instant dateAssignation;

    @Column(name = "date_cloture")
    private Instant dateCloture;

    @PrePersist
    void onCreate() {
        if (dateCreation == null) {
            dateCreation = Instant.now();
        }
        if (statut == null) {
            statut = StatutDemandeSOS.EN_ATTENTE;
        }
    }

    public DemandeSOS() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdChauffeur() { return idChauffeur; }
    public void setIdChauffeur(String idChauffeur) { this.idChauffeur = idChauffeur; }

    public String getIdCourseRef() { return idCourseRef; }
    public void setIdCourseRef(String idCourseRef) { this.idCourseRef = idCourseRef; }

    public TypePanne getTypePanne() { return typePanne; }
    public void setTypePanne(TypePanne typePanne) { this.typePanne = typePanne; }

    public NiveauUrgence getNiveauUrgence() { return niveauUrgence; }
    public void setNiveauUrgence(NiveauUrgence niveauUrgence) { this.niveauUrgence = niveauUrgence; }

    public Point getPosition() { return position; }
    public void setPosition(Point position) { this.position = position; }

    public StatutDemandeSOS getStatut() { return statut; }
    public void setStatut(StatutDemandeSOS statut) { this.statut = statut; }

    public Long getIdMecanicien() { return idMecanicien; }
    public void setIdMecanicien(Long idMecanicien) { this.idMecanicien = idMecanicien; }

    public Instant getDateCreation() { return dateCreation; }
    public void setDateCreation(Instant dateCreation) { this.dateCreation = dateCreation; }

    public Instant getDateAssignation() { return dateAssignation; }
    public void setDateAssignation(Instant dateAssignation) { this.dateAssignation = dateAssignation; }

    public Instant getDateCloture() { return dateCloture; }
    public void setDateCloture(Instant dateCloture) { this.dateCloture = dateCloture; }
}