package com.rideshare.rating.controller;

import com.rideshare.rating.dto.EvaluationRequest;
import com.rideshare.rating.dto.EvaluationUpdateRequest;
import com.rideshare.rating.dto.MoyenneResponse;
import com.rideshare.rating.model.Evaluation;
import com.rideshare.rating.service.EvaluationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    public Evaluation creer(@Valid @RequestBody EvaluationRequest request,
                            Authentication auth, HttpServletRequest http) {
        String token = http.getHeader("Authorization").substring(7);
        return service.creer(auth.getName(), request, token);
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
                               @Valid @RequestBody EvaluationUpdateRequest request,
                               Authentication auth) {
        return service.modifier(id, auth.getName(), request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String id, Authentication auth) {
        boolean admin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        service.supprimer(id, auth.getName(), admin);
    }
}
