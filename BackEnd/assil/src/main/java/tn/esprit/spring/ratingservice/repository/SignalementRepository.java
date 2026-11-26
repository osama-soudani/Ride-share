package tn.esprit.spring.ratingservice.repository;

import tn.esprit.spring.ratingservice.model.Signalement;
import tn.esprit.spring.ratingservice.model.StatutSignalement;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SignalementRepository extends MongoRepository<Signalement, String> {

    List<Signalement> findByStatut(StatutSignalement statut, Sort sort);

    List<Signalement> findByIdAuteurOrderByDateCreationDesc(String idAuteur);
}
