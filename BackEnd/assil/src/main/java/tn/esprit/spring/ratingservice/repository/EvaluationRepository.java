package tn.esprit.spring.ratingservice.repository;

import tn.esprit.spring.ratingservice.model.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EvaluationRepository extends MongoRepository<Evaluation, String> {

    Page<Evaluation> findByIdCible(String idCible, Pageable pageable);

    List<Evaluation> findByIdCourseRefOrderByDateDesc(String idCourseRef);
}
