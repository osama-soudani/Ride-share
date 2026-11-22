package com.rideshare.sos.repository;

import com.rideshare.sos.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {

    Optional<Intervention> findByIdDemande(Long idDemande);

    boolean existsByIdDemande(Long idDemande);

    boolean existsByIdMecanicien(Long idMecanicien);
}