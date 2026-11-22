package com.rideshare.rating.repository;

import com.rideshare.rating.model.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EvaluationRepository extends MongoRepository<Evaluation, String> {

    Page<Evaluation> findByIdCible(String idCible, Pageable pageable);

    List<Evaluation> findByIdCourseRefOrderByDateDesc(String idCourseRef);
}
