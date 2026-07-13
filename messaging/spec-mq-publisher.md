# spec-mq-publisher.md

## Description du module
Ce module ajoute une API REST permettant de publier un message de paiement vers IBM MQ sur la queue `DEV.QUEUE.1` (configurable via `app.mq.outbound-queue`).

## Role dans l'architecture
- Expose un point d'entree synchrone pour pousser des messages vers IBM MQ.
- Reutilise la couche JMS existante et la configuration centralisee `MqProperties`.
- Isole la logique de publication dans une Service Layer dediee.
- Normalise la gestion d'erreurs (validation et echec MQ) via `RestExceptionHandler`.

## Endpoints REST exposes
- `POST /api/v1/mq/messages`
  - Body JSON:
    - `payload` (string, obligatoire)
    - `correlationId` (string, optionnel, max 64)
  - Reponse 202:
    - `queue`
    - `correlationId`
    - `sentAt`
    - `status`

## Modele de donnees (entites, relations)
- Aucune nouvelle entite relationnelle.
- Ce module ne persiste pas les demandes de publication sortante.

## Diagramme logique
- `MqMessagePublisherController` -> `MqMessagePublisherService` -> `JmsTemplate` -> `IBM MQ (DEV.QUEUE.1)`

## Hypotheses et contraintes
- La queue de destination est configuree par `app.mq.outbound-queue` (valeur par defaut projet: `DEV.QUEUE.1`).
- L'authentification/autorisation API reste hors scope.
- En cas d'erreur JMS, l'API retourne `502 Bad Gateway`.

## Flux MQ concernes
- Flux sortant: `app.mq.outbound-queue` (dev: `DEV.QUEUE.1`).

