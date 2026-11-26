# ratingService (Assil)

Microservice RideShare : `Evaluation` et `Signalement` (CRUD complet).
Spring Boot 3.3 · Java 17 · MongoDB · Eureka client. Aucune authentification (JWT retiré).

## Lancer
1. Démarrer MongoDB (service Windows) et Eureka (port 8761)
2. Lancer `RatingServiceApplication` (port 8087)
3. Vérifier : http://localhost:8087/actuator/health et le tableau de bord Eureka (RATINGSERVICE)

## API (via la gateway : http://localhost:8086)
| Opération | Route |
|---|---|
| Create évaluation | POST /api/evaluations |
| Read all / one | GET /api/evaluations , /api/evaluations/{id} |
| Par cible / moyenne / course | GET /api/evaluations/cible/{id} , /cible/{id}/moyenne , /course/{id} |
| Update / Delete | PUT / DELETE /api/evaluations/{id} |
| Create signalement | POST /api/signalements |
| Read all / one / par auteur | GET /api/signalements?statut=OUVERT , /{id} , /auteur/{idAuteur} |
| Update / Traiter / Delete | PUT /api/signalements/{id} , PATCH /{id}/traiter , DELETE /{id} |

Exemple : POST /api/evaluations
{"idAuteur":"64b000000000000000000001","idCible":"64b000000000000000000002","idCourseRef":"64b000000000000000000003","note":5,"commentaire":"Très ponctuel"}
