package com.rideshare.sos.repository;

import com.rideshare.sos.entity.DemandeSOS;
import com.rideshare.sos.entity.enums.StatutDemandeSOS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface DemandeSOSRepository extends JpaRepository<DemandeSOS, Long> {

    List<DemandeSOS> findByIdChauffeur(String idChauffeur);

    boolean existsByIdMecanicienAndStatutNotIn(Long idMecanicien, Collection<StatutDemandeSOS> statuts);

    boolean existsByIdMecanicien(Long idMecanicien);
}