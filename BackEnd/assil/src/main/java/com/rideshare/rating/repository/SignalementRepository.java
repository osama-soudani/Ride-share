package com.rideshare.rating.repository;

import com.rideshare.rating.model.Signalement;
import com.rideshare.rating.model.StatutSignalement;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SignalementRepository extends MongoRepository<Signalement, String> {

    List<Signalement> findByStatut(StatutSignalement statut, Sort sort);

    List<Signalement> findByIdAuteurOrderByDateCreationDesc(String idAuteur);
}
