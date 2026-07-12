# Instructions Copilot – Génération de Spécifications Fonctionnelles (spec.md)

## 🎯 Objectif du fichier

Ce fichier définit **comment Copilot doit générer une spécification fonctionnelle complète** à partir d’un prompt fourni par l’utilisateur.  
La spécification doit être structurée, claire, exhaustive, et adaptée à un contexte bancaire critique (performance, résilience, volumétrie).

Chaque spec générée doit être produite dans un fichier nommé :


---

# 1. Détermination du Contexte – User Story

À partir du prompt fourni par l’utilisateur, Copilot doit :

1. **Analyser le besoin métier**.
2. **Déterminer le contexte fonctionnel**.
3. **Formuler une User Story complète**, structurée ainsi :

### Format obligatoire de la User Story


### Exemples d’acteurs métier
- Analyste Back Office
- Application de routage
- Système de paiement
- Administrateur MQ
- Application de supervision

Copilot doit choisir l’acteur le plus pertinent selon le prompt.

---

# 2. Règles de Gestion (Business Rules)

Copilot doit générer une section :


Les règles doivent être :

- **Claires**
- **Numérotées**
- **Non techniques**
- **Directement dérivées du prompt**
- **Adaptées au contexte bancaire IBM MQ**

### Exemples de règles de gestion
- RG1 : Un message doit être lu dès qu’il est disponible sur la file MQ.
- RG2 : Chaque message doit être persisté dans la base relationnelle avant tout traitement.
- RG3 : Les messages doivent être consultables via API REST.
- RG4 : Le système doit supporter une volumétrie élevée sans perte de messages.

Copilot doit générer entre **5 et 15 règles**, selon la complexité du prompt.

---

# 3. Critères d’Acceptation (Acceptance Criteria)

Copilot doit générer une section :


Chaque critère doit être formulé sous forme de scénario **Gherkin** :


Les critères doivent couvrir :

- Cas nominal
- Cas limites
- Cas d’erreur
- Cas volumétriques
- Cas de résilience (MQ indisponible, DB lente, etc.)

---

# 4. Tests Fonctionnels

Copilot doit générer une section :


Les tests doivent être :

- Numérotés
- Détaillés
- Directement dérivés des critères d’acceptation
- Adaptés au contexte MQ + API REST + DB

### Format obligatoire


---

# 5. Étapes High-Level (Plan de Réalisation)

Copilot doit générer une section :


Cette section doit décrire **les étapes macro** nécessaires pour implémenter la fonctionnalité.

### Format obligatoire


Copilot doit adapter cette liste selon le prompt.

---

# 6. Style attendu pour chaque spec générée

Copilot doit :

- Générer une spec **structurée**, **lisible**, **professionnelle**.
- Utiliser un langage **fonctionnel**, non technique.
- S’assurer que la spec est **cohérente**, **complète**, **non ambiguë**.
- Toujours inclure les sections suivantes :


---

# 7. Rappel pour Copilot

Quand tu génères une spec :

- Toujours analyser le prompt pour déterminer le contexte métier.
- Toujours structurer la spec selon les sections obligatoires.
- Toujours utiliser un style professionnel et clair.
- Toujours générer des règles de gestion pertinentes.
- Toujours générer des critères d’acceptation en Gherkin.
- Toujours générer des tests fonctionnels détaillés.
- Toujours générer un plan high-level réaliste.
- Toujours produire une spec complète, prête pour un analyste métier ou un développeur.

