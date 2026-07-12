# Instructions Copilot – Projet Java / Spring Boot – Département Paiement Banque

## 1. Contexte du projet

Le département de paiement de la banque reçoit des messages provenant des applications Back Office via une **file IBM MQ Series**.  
Ces messages transitent dans une **application de routage** pour être transférés vers différentes destinations internes ou externes.

### Objectifs de l’application à générer

Créer une application Web permettant :

1. **Lecture et stockage** des messages déposés sur une file IBM MQ Series dans une **base de données relationnelle**.
2. **Exposition d’API REST** permettant la **consultation des messages** via une IHM.
3. Répondre à des **contraintes de performance**, de **résilience**, et de **scalabilité**, étant donné la **volumétrie importante** de messages à traiter.

Copilot doit toujours tenir compte de ce contexte avant de générer du code.

---

## 2. Langage, version et stack technique

- Utiliser **Java 21** pour toute génération de code.
- Utiliser **Spring Boot** comme framework backend.
- Utiliser **Maven** comme système de build.
- Utiliser **IBM MQ** pour la réception des messages.
- Utiliser une **base relationnelle** (PostgreSQL ou autre).
- Utiliser **JUnit 5** + **Mockito** pour les tests.

---

## 3. Qualité du code et principes de conception

Copilot doit générer du code **comme un ingénieur Senior (10+ ans d’expérience)** :

### Principes POO obligatoires
- **Abstraction**
- **Encapsulation**
- **Polymorphisme**
- **Héritage**

### Design Patterns à utiliser dès que pertinent
- **Service Layer**
- **Repository**
- **Factory**
- **Builder**
- **Strategy**
- **Adapter**
- **Decorator**
- **Mapper**
- **DTO Pattern**

### Bonnes pratiques
- Code **lisible**, **structuré**, **propre**, **documenté**.
- Méthodes courtes, classes cohérentes, responsabilités bien définies.
- Nommage explicite et professionnel.
- Pas de duplication de code.
- Architecture claire :


---

## 4. Tests unitaires et couverture

Après **chaque génération de code**, Copilot doit :

- Générer les **tests unitaires associés**.
- Utiliser **JUnit 5** et **Mockito**.
- Tester :
- Cas nominal
- Cas limites
- Cas d’erreur
- Comportements asynchrones si MQ
- Assurer une **couverture minimale de 90%** sur les classes générées.
- Générer des tests pour :
- Services
- Repositories
- Controllers REST
- Composants MQ (mockés)

---

## 5. Documentation et fichiers de spécification

Après chaque génération de code, Copilot doit :

- Ajouter des **Javadoc** sur les classes et méthodes publiques.
- Documenter les choix techniques non évidents.
- Générer un fichier de spécification dans le projet :

### Format du fichier
`spec-<module>.md`

### Contenu obligatoire
- Description du module
- Rôle dans l’architecture
- Endpoints REST exposés
- Modèle de données (entités, relations)
- Diagramme logique si nécessaire
- Hypothèses et contraintes
- Flux MQ concernés

---

## 6. Style de génération attendu

Copilot doit :

1. **Analyser le contexte bancaire IBM MQ** avant de générer du code.
2. Générer du code **Java 21**, optimisé, robuste, maintenable.
3. Utiliser les **design patterns** quand c’est pertinent.
4. Générer les **tests unitaires** immédiatement après le code.
5. Assurer **90% de coverage minimum**.
6. Documenter le code et produire un fichier **spec-<module>.md**.
7. Générer du code **professionnel**, **optimisé**, **lisible**, **maintenable**.

---

## 7. Rappel pour Copilot

Quand tu génères du code dans ce dépôt :

- Toujours partir du **contexte bancaire IBM MQ**.
- Toujours utiliser **Java 21**.
- Toujours appliquer les **principes POO**.
- Toujours utiliser les **design patterns**.
- Toujours générer les **tests unitaires**.
- Toujours viser **90% de coverage**.
- Toujours documenter le code.
- Toujours générer un fichier de **spécification**.
- Toujours produire du code **professionnel**, **optimisé**, **lisible**, **maintenable**.

