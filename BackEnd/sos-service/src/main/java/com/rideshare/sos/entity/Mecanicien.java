package com.rideshare.sos.entity;

import com.rideshare.sos.entity.enums.Specialite;
import com.rideshare.sos.entity.enums.StatutMecanicien;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mecanicien")
public class Mecanicien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Logical reference to the auth-service account (a MongoDB ObjectId, so a String). */
    @Column(name = "id_compte", length = 24)
    private String idCompte;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 20)
    private String telephone;

    /** Stored in a small child table: mecanicien_specialites. */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mecanicien_specialites",
            joinColumns = @JoinColumn(name = "mecanicien_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "specialite", nullable = false, length = 30)
    private Set<Specialite> specialites = new HashSet<>();

    /** PostGIS point, SRID 4326. In a Point, x = longitude and y = latitude. */
    @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point position;

    @Column(name = "rayon_intervention_km", nullable = false)
    private Double rayonInterventionKm;

    @Column(nullable = false)
    private boolean disponible;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutMecanicien statut;

    @Column(name = "note_moyenne")
    private Double noteMoyenne;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private Instant dateCreation;

    @PrePersist
    void onCreate() {
        if (dateCreation == null) {
            dateCreation = Instant.now();
        }
    }

    public Mecanicien() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdCompte() { return idCompte; }
    public void setIdCompte(String idCompte) { this.idCompte = idCompte; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Set<Specialite> getSpecialites() { return specialites; }
    public void setSpecialites(Set<Specialite> specialites) { this.specialites = specialites; }

    public Point getPosition() { return position; }
    public void setPosition(Point position) { this.position = position; }

    public Double getRayonInterventionKm() { return rayonInterventionKm; }
    public void setRayonInterventionKm(Double rayonInterventionKm) { this.rayonInterventionKm = rayonInterventionKm; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public StatutMecanicien getStatut() { return statut; }
    public void setStatut(StatutMecanicien statut) { this.statut = statut; }

    public Double getNoteMoyenne() { return noteMoyenne; }
    public void setNoteMoyenne(Double noteMoyenne) { this.noteMoyenne = noteMoyenne; }

    public Instant getDateCreation() { return dateCreation; }
    public void setDateCreation(Instant dateCreation) { this.dateCreation = dateCreation; }
}