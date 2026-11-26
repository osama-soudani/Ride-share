# payment-service — RideShare

Microservice Spring Boot responsable du solde prépayé des chauffeurs et du
prélèvement automatique de la commission de la plateforme à la clôture de
chaque course.

## Stack technique
- Java 17, Spring Boot 3.2.5
- Spring Web, Spring Data JPA, PostgreSQL
- Spring Security (OAuth2 Resource Server) — valide les JWT émis par Keycloak
- Lombok
- Tests : JUnit 5, Mockito, AssertJ

## Démarrer en local

### 1. Lancer la base PostgreSQL
```bash
docker compose up -d
```
Cela démarre une base `payment_db` sur `localhost:5432` (utilisateur
`payment_user` / mot de passe `payment_pass`, voir `docker-compose.yml`).

### 2. Configurer Keycloak (si déjà disponible dans votre environnement)
Le service valide les JWT via l'issuer configuré dans
`application.properties` :
```
spring.security.oauth2.resourceserver.jwt.issuer-uri=${KEYCLOAK_ISSUER_URI:http://localhost:8080/realms/rideshare}
```
Si Keycloak n'est pas encore en place dans votre équipe, vous pouvez
temporairement commenter la configuration de sécurité dans
`SecurityConfig.java` pour tester les endpoints sans jeton — à ne jamais
faire en production.

### 3. Lancer le service
```bash
mvn spring-boot:run
```
Le service démarre sur `http://localhost:8085` (configurable via
`SERVER_PORT`).

### 4. Lancer les tests
```bash
mvn test
```
Les tests unitaires (`PaymentServiceImplTest`) couvrent la logique critique :
débit de commission avec solde suffisant, refus si solde insuffisant (le
solde ne doit alors pas être modifié), recharge, et création automatique
d'un solde à zéro pour un chauffeur inconnu.

## Variables d'environnement

| Variable | Description | Défaut |
|---|---|---|
| `SERVER_PORT` | Port d'écoute du service | 8085 |
| `DB_HOST` / `DB_PORT` / `DB_NAME` / `DB_USER` / `DB_PASSWORD` | Connexion PostgreSQL | voir `application.properties` |
| `KEYCLOAK_ISSUER_URI` | URL de l'issuer Keycloak pour la validation JWT | `http://localhost:8080/realms/rideshare` |
| `COMMISSION_RATE` | Taux de commission appliqué (ex: 0.10 = 10%) | 0.10 |

## Endpoints

| Méthode | Endpoint | Description | Codes |
|---|---|---|---|
| GET | `/api/payments/balance/{driverId}` | Consulter le solde d'un chauffeur | 200, 404 |
| POST | `/api/payments/recharge` | Enregistrer une recharge | 201, 400 |
| POST | `/api/payments/commission` | Prélever la commission d'une course (endpoint interne) | 201, 402, 400 |
| GET | `/api/payments/history/{driverId}` | Historique paginé des transactions | 200 |
| GET | `/api/payments/balances?page=0&size=20` | Lister tous les soldes | 200 |
| GET | `/api/payments/balances/{soldeId}` | Consulter un solde par son ID | 200, 404 |
| POST | `/api/payments/balances` | Créer un solde | 201, 409, 400 |
| PUT | `/api/payments/balances/{soldeId}` | Modifier le montant d'un solde | 200, 404, 400 |
| DELETE | `/api/payments/balances/{soldeId}` | Supprimer un solde et ses transactions | 204, 404 |

Exemple de corps pour `POST /api/payments/recharge` :
```json
{
  "driverId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "montant": 50.00
}
```

Exemple de corps pour `POST /api/payments/commission` :
```json
{
  "driverId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "courseId": "9c858901-8a57-4791-81fe-4c455b099bc9",
  "montantCourse": 20.00
}
```

## Points d'attention pour la suite

- **Concurrence** : le débit de commission utilise un verrou pessimiste
  (`findByIdChauffeurForUpdate`) en plus du verrouillage optimiste
  (`@Version`), pour éviter qu'un chauffeur se retrouve avec un solde
  négatif en cas de deux prélèvements simultanés.
- **Migration de schéma** : `spring.jpa.hibernate.ddl-auto=update` convient
  pour le développement, mais devra être remplacé par des migrations
  versionnées (Flyway ou Liquibase) avant tout déploiement en production.
- **Endpoint interne** : `/api/payments/commission` est protégé par un rôle
  `SERVICE_INTERNAL` ou `ADMIN` — à faire correspondre avec la configuration
  réelle du "client service account" dans Keycloak une fois disponible.
