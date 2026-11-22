# rating-service (Spring Boot)

Microservice RideShare : `Evaluation` et `Signalement`. Java 17, Spring Boot 3.3, MongoDB, JWT.

## Lancer
```bash
export MONGO_URI=mongodb://localhost:27017/rating_db
export JWT_SECRET=<même secret que auth-service, 32+ caractères>
mvn spring-boot:run
```
Port : 8087. Santé : GET /actuator/health

## API CRUD (Authorization: Bearer <JWT>)

### Evaluation
| Opération | Route | Qui |
|---|---|---|
| Create | POST /api/evaluations | PASSAGER, CHAUFFEUR |
| Read all | GET /api/evaluations?page=0&size=10 | authentifié |
| Read one | GET /api/evaluations/{id} | authentifié |
| Read | GET /api/evaluations/cible/{idCible} , /cible/{idCible}/moyenne , /course/{idCourse} | authentifié |
| Update | PUT /api/evaluations/{id} (note, commentaire) | auteur uniquement |
| Delete | DELETE /api/evaluations/{id} | auteur ou ADMIN |

### Signalement
| Opération | Route | Qui |
|---|---|---|
| Create | POST /api/signalements | PASSAGER, CHAUFFEUR |
| Read all | GET /api/signalements?statut=OUVERT | ADMIN |
| Read one | GET /api/signalements/{id} | ADMIN |
| Read | GET /api/signalements/mes-signalements | authentifié |
| Update | PUT /api/signalements/{id} (motif) | auteur, si statut OUVERT |
| Traiter | PATCH /api/signalements/{id}/traiter | ADMIN |
| Delete | DELETE /api/signalements/{id} | ADMIN |
