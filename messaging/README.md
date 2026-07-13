# Messaging Bootstrap

Initialisation d'une application Spring Boot (Java 21) pour reception IBM MQ, persistance PostgreSQL et consultation REST.

## Prerequis
- Java 21
- Docker ou instances accessibles pour PostgreSQL et IBM MQ

## Configuration
Les valeurs par defaut se trouvent dans `src/main/resources/application.properties`.

Variables a adapter:
- JDBC PostgreSQL (`spring.datasource.*`)
- MQ (`app.mq.*`)

## Endpoints
- `GET /api/v1/messages`
- `GET /api/v1/messages/{id}`

## Lancement local
```powershell
./mvnw spring-boot:run
```

## Tests
```powershell
./mvnw test
```

