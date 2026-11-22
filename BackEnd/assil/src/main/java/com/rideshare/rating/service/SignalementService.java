package com.rideshare.rating.service;

import com.rideshare.rating.dto.SignalementRequest;
import com.rideshare.rating.dto.SignalementUpdateRequest;
import com.rideshare.rating.model.Signalement;
import com.rideshare.rating.model.StatutSignalement;
import com.rideshare.rating.repository.SignalementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignalementService {

    private final SignalementRepository repository;

    // ---------- CREATE ----------
    public Signalement creer(String idAuteur, SignalementRequest req) {
        if (idAuteur.equals(req.idCible())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Vous ne pouvez pas vous signaler vous-même");
        }
        return repository.save(Signalement.builder()
                .idAuteur(idAuteur)
                .idCible(req.idCible())
                .motif(req.motif())
                .statut(StatutSignalement.OUVERT)
                .dateCreation(Instant.now())
                .build());
    }

    // ---------- READ ----------
    public List<Signalement> lister(StatutSignalement statut) {
        Sort sort = Sort.by(Sort.Direction.DESC, "dateCreation");
        return statut == null ? repository.findAll(sort) : repository.findByStatut(statut, sort);
    }

    public List<Signalement> mesSignalements(String idAuteur) {
        return repository.findByIdAuteurOrderByDateCreationDesc(idAuteur);
    }

    public Signalement obtenir(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Signalement introuvable"));
    }

    // ---------- UPDATE (motif : auteur uniquement, tant que OUVERT) ----------
    public Signalement modifier(String id, String idUtilisateur, SignalementUpdateRequest req) {
        Signalement s = obtenir(id);
        if (!s.getIdAuteur().equals(idUtilisateur)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Seul l'auteur peut modifier son signalement");
        }
        if (s.getStatut() == StatutSignalement.TRAITE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Un signalement déjà traité ne peut plus être modifié");
        }
        s.setMotif(req.motif());
        return repository.save(s);
    }

    /** Signalement.traiter() : OUVERT -> TRAITE (ADMIN) */
    public Signalement traiter(String id) {
        Signalement s = obtenir(id);
        if (s.getStatut() == StatutSignalement.TRAITE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Signalement déjà traité");
        }
        s.setStatut(StatutSignalement.TRAITE);
        return repository.save(s);
    }

    // ---------- DELETE (ADMIN) ----------
    public void supprimer(String id) {
        repository.delete(obtenir(id));
    }
}
