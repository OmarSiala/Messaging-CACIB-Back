# Configuration par Environnement

Ce projet utilise des profils Spring Boot pour gérer les configurations par environnement : **dev**, **hom** (homologation), et **prod** (production).

## Structure des fichiers

```
src/main/resources/
├── application.properties           # Propriétés communes à tous les profils
├── application-dev.properties       # Configuration développement (localhost)
├── application-hom.properties       # Configuration homologation (préproduction)
└── application-prod.properties      # Configuration production
```

## Profils disponibles

### 1. **DEV** (Développement - Default)

**Fichier:** `application-dev.properties`

- **Base de données:** PostgreSQL sur `localhost:5432`
- **IBM MQ:** Connexion locale `localhost:1414`
- **Logging:** DEBUG pour l'application, INFO pour le reste
- **Résilience JMS:** Min 3 / Max 10 threads

**Utilisation:**
```bash
java -jar messaging.jar --spring.profiles.active=dev
# ou laissez le profil par défaut (dev est activé par défaut)
java -jar messaging.jar
```

### 2. **HOM** (Homologation - Préproduction)

**Fichier:** `application-hom.properties`

- **Base de données:** PostgreSQL sur `hom-db.internal:5432`
- **IBM MQ:** Queue manager HOM_QM1 sur `hom-mq.internal:1414`
- **Logging:** INFO pour l'application, WARN pour le reste
- **Résilience JMS:** Min 5 / Max 15 threads
- **Authentification:** Via variables d'environnement (`DB_USERNAME`, `DB_PASSWORD`, `MQ_USERNAME`, `MQ_PASSWORD`)

**Utilisation:**
```bash
export DB_USERNAME=hom_user
export DB_PASSWORD=secure_password
export MQ_USERNAME=mq_user
export MQ_PASSWORD=mq_password
java -jar messaging.jar --spring.profiles.active=hom
```

### 3. **PROD** (Production)

**Fichier:** `application-prod.properties`

- **Base de données:** PostgreSQL sur `prod-db.internal:5432`
- **IBM MQ:** Queue manager PROD_QM1 avec failover (`prod-mq1.internal` + `prod-mq2.internal`)
- **Logging:** INFO seulement pour l'application, WARN pour le reste
- **Résilience JMS:** Min 10 / Max 50 threads
- **Connection Pool:** Hikari optimisé (Max 20, Min 5)
- **Authentification:** Via variables d'environnement
- **Performance:** Tomcat optimisé (Max 200 threads)

**Utilisation:**
```bash
export DB_USERNAME=prod_user
export DB_PASSWORD=prod_secure_password
export MQ_USERNAME=mq_prod_user
export MQ_PASSWORD=mq_prod_password
java -jar messaging.jar --spring.profiles.active=prod
```

## Variables d'environnement

Pour **hom** et **prod**, les identifiants sensibles doivent être fournis via variables d'environnement :

| Variable | Profils | Description |
|----------|---------|-------------|
| `DB_USERNAME` | hom, prod | Identifiant PostgreSQL |
| `DB_PASSWORD` | hom, prod | Mot de passe PostgreSQL |
| `MQ_USERNAME` | hom, prod | Identifiant IBM MQ |
| `MQ_PASSWORD` | hom, prod | Mot de passe IBM MQ |

## Configuration locale sans profil

Si aucun profil n'est spécifié, **dev** est activé par défaut (voir `spring.profiles.active=dev` dans `application.properties`).

## Propriétés communes

Les propriétés suivantes sont communes à tous les profils (définies dans `application.properties`) :

- `spring.application.name=messaging`
- `spring.datasource.driver-class-name=org.postgresql.Driver`
- `spring.jpa.hibernate.ddl-auto=validate`
- `spring.jpa.open-in-view=false`
- `spring.jpa.properties.hibernate.jdbc.time_zone=UTC`
- `spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml`

## Déploiement Docker

### Développement
```dockerfile
FROM openjdk:21-slim
COPY target/messaging.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]
```

### Homologation
```dockerfile
FROM openjdk:21-slim
COPY target/messaging.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=hom"]
```

### Production
```dockerfile
FROM openjdk:21-slim
COPY target/messaging.jar app.jar
ENV JAVA_OPTS="-Xmx1024m -Xms512m"
ENTRYPOINT ["java", "$JAVA_OPTS", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

## Vérifier le profil actif au démarrage

Les logs au démarrage affichent le profil actif :

```
2026-07-13T12:00:00.000+02:00  INFO ... : No active profile set, falling back to 1 default profile: "dev"
```

Ou si explicitement défini :

```
2026-07-13T12:00:00.000+02:00  INFO ... : The following profiles are active: prod
```

## Bonnes pratiques

1. **Ne jamais** commiter les identifiants en dur dans les fichiers de configuration.
2. **Utiliser** des variables d'environnement pour hom/prod.
3. **Tester** chaque profil avant déploiement.
4. **Monitorer** les logs pour confirmer le profil actif.
5. **Documenter** les changements de configuration dans un fichier CHANGELOG.

---

*Dernière mise à jour : 2026-07-13*

