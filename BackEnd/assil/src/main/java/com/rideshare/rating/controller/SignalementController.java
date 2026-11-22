package com.rideshare.rating.controller;

import com.rideshare.rating.dto.SignalementRequest;
import com.rideshare.rating.dto.SignalementUpdateRequest;
import com.rideshare.rating.model.Signalement;
import com.rideshare.rating.model.StatutSignalement;
import com.rideshare.rating.service.SignalementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/signalements")
@RequiredArgsConstructor
public class SignalementController {

    private final SignalementService service;

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Signalement creer(@Valid @RequestBody SignalementRequest request, Authentication auth) {
        return service.creer(auth.getName(), request);
    }

    // READ
    @GetMapping("/mes-signalements")
    public List<Signalement> mesSignalements(Authentication auth) {
        return service.mesSignalements(auth.getName());
    }

    /** ADMIN : GET /api/signalements?statut=OUVERT */
    @GetMapping
    public List<Signalement> lister(@RequestParam(required = false) StatutSignalement statut) {
        return service.lister(statut);
    }

    /** ADMIN */
    @GetMapping("/{id}")
    public Signalement obtenir(@PathVariable String id) {
        return service.obtenir(id);
    }

    // UPDATE (auteur, tant que OUVERT)
    @PutMapping("/{id}")
    public Signalement modifier(@PathVariable String id,
                                @Valid @RequestBody SignalementUpdateRequest request,
                                Authentication auth) {
        return service.modifier(id, auth.getName(), request);
    }

    /** ADMIN : OUVERT -> TRAITE */
    @PatchMapping("/{id}/traiter")
    public Signalement traiter(@PathVariable String id) {
        return service.traiter(id);
    }

    // DELETE (ADMIN)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String id) {
        service.supprimer(id);
    }
}
