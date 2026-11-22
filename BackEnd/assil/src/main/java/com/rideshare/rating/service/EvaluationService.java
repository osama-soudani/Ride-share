package com.rideshare.rating.service;

import com.rideshare.rating.dto.EvaluationRequest;
import com.rideshare.rating.dto.EvaluationUpdateRequest;
import com.rideshare.rating.dto.MoyenneResponse;
import com.rideshare.rating.model.Evaluation;
import com.rideshare.rating.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository repository;
    private final MongoTemplate mongoTemplate;
    private final MatchingClient matchingClient;

    // ---------- CREATE ----------
    public Evaluation creer(String idAuteur, EvaluationRequest req, String bearerToken) {
        if (idAuteur.equals(req.idCible())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Vous ne pouvez pas vous évaluer vous-même");
        }

        matchingClient.verifierParticipation(req.idCourseRef(), idAuteur, req.idCible(), bearerToken);

        Evaluation evaluation = Evaluation.builder()
                .idAuteur(idAuteur)
                .idCible(req.idCible())
                .idCourseRef(req.idCourseRef())
                .note(req.note())
                .commentaire(req.commentaire())
                .date(Instant.now())
                .build();
        // DuplicateKeyException (index unique) -> 409 via GlobalExceptionHandler
        return repository.save(evaluation);
    }

    // ---------- READ ----------
    public Page<Evaluation> lister(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Evaluation obtenir(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Évaluation introuvable"));
    }

    public Page<Evaluation> listerParCible(String idCible, Pageable pageable) {
        verifierObjectId(idCible);
        return repository.findByIdCible(idCible, pageable);
    }

    public List<Evaluation> listerParCourse(String idCourse) {
        verifierObjectId(idCourse);
        return repository.findByIdCourseRefOrderByDateDesc(idCourse);
    }

    public MoyenneResponse moyenneParCible(String idCible) {
        verifierObjectId(idCible);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("idCible").is(new ObjectId(idCible))),
                Aggregation.group().avg("note").as("moyenne").count().as("nombre"));

        Document result = mongoTemplate.aggregate(agg, "evaluations", Document.class)
                .getUniqueMappedResult();

        if (result == null) return new MoyenneResponse(idCible, null, 0);
        double moyenne = ((Number) result.get("moyenne")).doubleValue();
        long nombre = ((Number) result.get("nombre")).longValue();
        return new MoyenneResponse(idCible, Math.round(moyenne * 100) / 100.0, nombre);
    }

    // ---------- UPDATE (auteur uniquement ; la cible et la course ne changent pas) ----------
    public Evaluation modifier(String id, String idUtilisateur, EvaluationUpdateRequest req) {
        Evaluation e = obtenir(id);
        if (!e.getIdAuteur().equals(idUtilisateur)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Seul l'auteur peut modifier son évaluation");
        }
        e.setNote(req.note());
        e.setCommentaire(req.commentaire());
        return repository.save(e);
    }

    // ---------- DELETE (auteur ou ADMIN) ----------
    public void supprimer(String id, String idUtilisateur, boolean admin) {
        Evaluation e = obtenir(id);
        if (!admin && !e.getIdAuteur().equals(idUtilisateur)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Seul l'auteur ou un administrateur peut supprimer cette évaluation");
        }
        repository.delete(e);
    }

    private void verifierObjectId(String id) {
        if (!ObjectId.isValid(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identifiant invalide : " + id);
        }
    }
}
