package tn.esprit.spring.ratingservice.controller;

import tn.esprit.spring.ratingservice.dto.SignalementRequest;
import tn.esprit.spring.ratingservice.dto.SignalementUpdateRequest;
import tn.esprit.spring.ratingservice.model.Signalement;
import tn.esprit.spring.ratingservice.model.StatutSignalement;
import tn.esprit.spring.ratingservice.service.SignalementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public Signalement creer( @RequestBody SignalementRequest request) {
        return service.creer(request);
    }

    // READ
    /** GET /api/signalements?statut=OUVERT */
    @GetMapping
    public List<Signalement> lister(@RequestParam(required = false) StatutSignalement statut) {
        return service.lister(statut);
    }

    @GetMapping("/{id}")
    public Signalement obtenir(@PathVariable String id) {
        return service.obtenir(id);
    }

    @GetMapping("/auteur/{idAuteur}")
    public List<Signalement> parAuteur(@PathVariable String idAuteur) {
        return service.parAuteur(idAuteur);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Signalement modifier(@PathVariable String id,
                                @Valid @RequestBody SignalementUpdateRequest request) {
        return service.modifier(id, request);
    }

    /** OUVERT -> TRAITE */
    @PatchMapping("/{id}/traiter")
    public Signalement traiter(@PathVariable String id) {
        return service.traiter(id);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String id) {
        service.supprimer(id);
    }
}
