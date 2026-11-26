package com.rideshare.payment.repository;

import com.rideshare.payment.entity.SoldeChauffeur;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SoldeChauffeurRepository extends JpaRepository<SoldeChauffeur, UUID> {

    Optional<SoldeChauffeur> findByIdChauffeur(UUID idChauffeur);

    /**
     * Verrou pessimiste sur la ligne du solde pendant un débit de commission,
     * en complément du verrouillage optimiste (@Version) : évite qu'un deuxième
     * débit concurrent sur le même chauffeur ne lise un solde déjà périmé.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SoldeChauffeur s where s.idChauffeur = :idChauffeur")
    Optional<SoldeChauffeur> findByIdChauffeurForUpdate(@Param("idChauffeur") UUID idChauffeur);
}
