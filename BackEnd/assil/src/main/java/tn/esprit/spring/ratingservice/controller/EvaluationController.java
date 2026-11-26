package tn.esprit.spring.ratingservice.controller;

import tn.esprit.spring.ratingservice.dto.EvaluationRequest;
import tn.esprit.spring.ratingservice.dto.EvaluationUpdateRequest;
import tn.esprit.spring.ratingservice.dto.MoyenneResponse;
import tn.esprit.spring.ratingservice.model.Evaluation;
import tn.esprit.spring.ratingservice.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService service;

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Evaluation creer(@Valid @RequestBody EvaluationRequest request) {
        return service.creer(request);
    }

    // READ
    @GetMapping
    public Page<Evaluation> lister(
            @PageableDefault(size = 10, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.lister(pageable);
    }

    @GetMapping("/{id}")
    public Evaluation obtenir(@PathVariable String id) {
        return service.obtenir(id);
    }

    @GetMapping("/cible/{idCible}")
    public Page<Evaluation> listerParCible(
            @PathVariable String idCible,
            @PageableDefault(size = 10, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.listerParCible(idCible, pageable);
    }

    @GetMapping("/cible/{idCible}/moyenne")
    public MoyenneResponse moyenne(@PathVariable String idCible) {
        return service.moyenneParCible(idCible);
    }

    @GetMapping("/course/{idCourse}")
    public List<Evaluation> listerParCourse(@PathVariable String idCourse) {
        return service.listerParCourse(idCourse);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Evaluation modifier(@PathVariable String id,
                               @Valid @RequestBody EvaluationUpdateRequest request) {
        return service.modifier(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String id) {
        service.supprimer(id);
    }
}
