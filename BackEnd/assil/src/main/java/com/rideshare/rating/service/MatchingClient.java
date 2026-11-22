package com.rideshare.rating.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Vérification optionnelle auprès du matching-service (GroupeTrajet).
 * Hypothèse : GET /api/groupes/{id} -> { statut, idChauffeur, idsPassagers: [] }
 * À adapter à l'API réelle de votre matching-service.
 */
@Component
public class MatchingClient {

    public record GroupeDto(String statut, String idChauffeur, List<String> idsPassagers) {}

    private final RestClient restClient;
    private final boolean enabled;

    public MatchingClient(@Value("${rating.matching-service-url}") String baseUrl,
                          @Value("${rating.verify-course}") boolean enabled) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.enabled = enabled;
    }

    public void verifierParticipation(String idCourseRef, String idAuteur, String idCible, String bearerToken) {
        if (!enabled) return;

        GroupeDto groupe;
        try {
            groupe = restClient.get()
                    .uri("/api/groupes/{id}", idCourseRef)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()
                    .body(GroupeDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course introuvable");
        }

        if (groupe == null || !"TERMINE".equals(groupe.statut())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La course n'est pas terminée");
        }
        List<String> participants = new ArrayList<>();
        participants.add(groupe.idChauffeur());
        if (groupe.idsPassagers() != null) participants.addAll(groupe.idsPassagers());

        if (!participants.contains(idAuteur) || !participants.contains(idCible)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "L'auteur ou la cible n'a pas participé à la course");
        }
    }
}
