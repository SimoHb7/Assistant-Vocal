# Plan de Projet : Assistant Financier Intelligent pour Inclusion Bancaire Rurale

## Vue d'Ensemble

Ce projet développe une application mobile d'assistant financier intelligent pour l'inclusion bancaire rurale. Il utilise Flutter pour le frontend et Spring Boot pour le backend, en intégrant l'IA pour des conseils financiers personnalisés et un support vocal en anglais, français, arabe et darija marocain. L'application offre une évaluation d'éligibilité aux prêts, des recommandations d'épargne, des outils de budgétisation et des interactions vocales.

## Fonctionnalités Clés

- **Services Financiers** : Conseils sur les prêts, plans d'épargne, assistance budgétaire
- **Capacités IA** : NLP de base pour le chat et la conversion voix-texte utilisant des APIs gratuites
- **Support Vocal** : Reconnaissance et synthèse vocale multi-langues
- **Focus Rural** : Conçu pour les utilisateurs ruraux non bancarisés avec une interface simple et accessible

## Pile Technologique (Focus Gratuit/Open-Source)

- **Backend** : Spring Boot (Java) pour l'API REST
- **Frontend** : Flutter (Dart) pour l'application mobile cross-platform
- **IA** : Hugging Face Transformers (modèles gratuits pour NLP)
- **Traitement Vocal** : Plugins Flutter (speech_to_text, flutter_tts) avec quota gratuit de Google Speech API
- **Base de Données** : PostgreSQL avec Docker
- **Conteneurisation** : Docker pour le déploiement
- **Déploiement** : Heroku free tier ou local pour projet scolaire

## Architecture de Haut Niveau

- **Application Mobile (Flutter)** : Gère l'UI, l'entrée/sortie vocale, les appels API
- **Backend (Spring Boot)** : Traite les requêtes, intègre l'IA, gère les données
- **Couche IA** : APIs externes gratuites pour le traitement des requêtes financières

## Diagrammes

### Diagramme de Cas d'Utilisation

```mermaid
graph TD
    A[Utilisateur Rural] --> B[Demander un Conseil Financier]
    A --> C[Utiliser l'Entrée Vocale]
    A --> D[Voir les Recommandations d'Épargne]
    A --> E[Vérifier l'Éligibilité au Prêt]
    A --> F[Accéder aux Outils de Budgétisation]
    B --> G[IA Traite la Requête]
    C --> G
    D --> G
    E --> G
    F --> G
    G --> H[Recevoir une Réponse Personnalisée]
    I[Administrateur Bancaire] --> J[Voir les Analyses Utilisateur]
    I --> K[Gérer les Paramètres Système]
    I --> L[Surveiller l'Utilisation]
```

### Diagramme de Classes

```mermaid
classDiagram
    class Utilisateur {
        +String userId
        +String nom
        +String languePreferee
        +List<Transaction> transactions
        +getProfilFinancier()
        +mettreAJourProfil()
    }
    class ConseillerFinancier {
        +String requete
        +String reponse
        +genererConseil(Utilisateur, requete)
        +analyserRisques()
    }
    class ProcesseurVocal {
        +String langue
        +processSpeechToText(entreeAudio)
        +genererTextToSpeech(sortieTexte)
        +changerLangue()
    }
    class ControleurAPI {
        +handleRequeteFinanciere()
        +handleRequeteVocale()
        +authentifierUtilisateur()
    }
    class ModeleIA {
        +processNLP(requete)
        +entrainerModele()
    }
    class BaseDeDonnees {
        +sauvegarderUtilisateur()
        +recupererTransactions()
    }
    Utilisateur --> ConseillerFinancier
    ConseillerFinancier --> ProcesseurVocal
    ControleurAPI --> ConseillerFinancier
    ConseillerFinancier --> ModeleIA
    ControleurAPI --> BaseDeDonnees
    Utilisateur --> BaseDeDonnees
```

### Diagramme de Séquence

```mermaid
sequenceDiagram
    participant UtilisateurRural
    participant ApplicationFlutter
    participant APISpringBoot
    participant IAModel

    UtilisateurRural->>ApplicationFlutter: Parle une requête financière en langue locale
    ApplicationFlutter->>APISpringBoot: POST /api/conseil avec texte/audio
    APISpringBoot->>BaseDeDonnees: Vérifier le profil utilisateur
    BaseDeDonnees-->>APISpringBoot: Retourner les données
    APISpringBoot->>IAModel: Envoyer la requête pour traitement NLP
    IAModel-->>APISpringBoot: Retourner le conseil traité
    APISpringBoot->>ApplicationFlutter: Réponse JSON avec conseil
    ApplicationFlutter->>UtilisateurRural: Afficher le conseil et parler la réponse
```

### Diagramme de Gantt

```mermaid
gantt
    title Chronologie de Développement du Projet
    dateFormat YYYY-MM-DD
    section Planification
    Exigences et Portée :done, req, 2024-12-01, 2024-12-07
    Conception Architecture :done, arch, 2024-12-08, 2024-12-14
    section Développement
    Configuration Backend (Spring Boot) :dev_back, 2024-12-15, 2025-01-05
    Intégration IA (Hugging Face) :ai_int, 2025-01-06, 2025-01-19
    Développement Frontend (Flutter) :dev_front, 2024-12-15, 2025-01-19
    Implémentation Fonctionnalités Vocales :voice_impl, 2025-01-20, 2025-02-02
    Configuration Docker et PostgreSQL :docker_db, 2025-01-20, 2025-01-26
    section Tests et Intégration
    Tests Unitaires :unit_test, 2025-02-03, 2025-02-09
    Tests d'Intégration :int_test, 2025-02-10, 2025-02-16
    Tests d'Acceptation Utilisateur :uat, 2025-02-17, 2025-02-23
    section Déploiement
    Ajustements Finaux et Déploiement :deploy, 2025-02-24, 2025-03-02
```

## Meilleures Pratiques pour l'Excellence

- **Sécurité** : Implémenter l'authentification JWT, le chiffrement des données
- **Évolutivité** : Utiliser l'architecture microservices dans Spring Boot
- **Accessibilité** : Assurer que les fonctionnalités vocales fonctionnent hors ligne si possible
- **Tests** : Tests unitaires et d'intégration complets
- **Documentation** : Docs API avec Swagger, commentaires de code

Ce plan positionne le projet comme un devoir scolaire de haute qualité avec une applicabilité réelle.