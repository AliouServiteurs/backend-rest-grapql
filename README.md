# 📱 Application CRUD - REST & GraphQL

Application full-stack de gestion de personnes utilisant Spring Boot (Backend) avec REST API et GraphQL, et React (Frontend).

## 📋 Table des matières

- [Aperçu](#aperçu)
- [Technologies](#technologies)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Utilisation](#utilisation)
- [Architecture](#architecture)
- [API Documentation](#api-documentation)
- [Tests](#tests)
- [Auteur](#auteur)

---

## 🎯 Aperçu

Cette application permet de gérer une base de données de personnes avec les fonctionnalités CRUD complètes :

- ✅ **Créer** une personne (REST POST)
- ✅ **Modifier** une personne (REST PUT)
- ✅ **Supprimer** une personne (REST DELETE)
- ✅ **Lister** toutes les personnes (GraphQL Query)
- ✅ **Rechercher** des personnes avec filtres (GraphQL Query)

### Particularités

- **Validation des données** avec Jakarta Validation
- **Normalisation automatique** (nom en MAJUSCULES, prénom capitalisé)
- **Gestion d'erreurs** professionnelle (REST et GraphQL)
- **Unicité du téléphone** vérifiée
- **Logs** détaillés pour le debugging

---

## 🛠️ Technologies

### Backend

- **Framework** : Spring Boot 3.x
- **Langage** : Java 17
- **Build Tool** : Maven
- **Base de données** : MariaDB 10.x
- **ORM** : Hibernate (JPA)
- **APIs** : 
  - REST (Spring Web)
  - GraphQL (Spring for GraphQL)

### Dépendances principales
```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-graphql
- spring-boot-starter-validation
- mariadb-java-client
- lombok
```

### Frontend (À venir)

- React 18.x
- Axios (REST API)
- Apollo Client (GraphQL)

---

## ⚙️ Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Java JDK 17** ou supérieur
- **Maven 3.8+**
- **MariaDB 10.x** ou MySQL 8.x
- **Git**
- **IDE** : IntelliJ IDEA, Eclipse, ou VS Code

---

## 🚀 Installation

### 1. Cloner le repository
```bash
git clone https://github.com/AliouServiteurs/backend-rest-graphql.git
cd backend-rest-graphql
```

### 2. Créer la base de données

Connectez-vous à MariaDB et exécutez :
```sql
CREATE DATABASE IF NOT EXISTS examen_rs_db;
```

### 3. Configurer l'application

Modifier `src/main/resources/application.properties` :
```properties
# Configuration Base de Données
spring.datasource.url=jdbc:mariadb://localhost:3306/examen_rs_db
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# GraphQL
spring.graphql.graphiql.enabled=true
# Activer GraphiQL (interface de test GraphQL)
spring.graphql.graphiql.path=/graphiql

# URL de l'endpoint GraphQL
spring.graphql.path=/graphql

# Serveur
server.port=8080
```

### 4. Compiler et démarrer
```bash
# Compiler le projet
mvn clean install

# Démarrer l'application
mvn spring-boot:run
```

L'application sera accessible sur **http://localhost:8080**

---

## 📝 Configuration

### Structure de la base de données

La table `personne` sera créée automatiquement avec la structure suivante :

| Colonne | Type | Contraintes |
|---------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| nom | VARCHAR(100) | NOT NULL |
| prenom | VARCHAR(100) | NOT NULL |
| date_naissance | DATE | - |
| adresse | VARCHAR(255) | - |
| telephone | VARCHAR(20) | - |

### Variables d'environnement (optionnel)

Vous pouvez définir ces variables pour personnaliser la configuration :
```bash
export DB_URL=jdbc:mariadb://localhost:3306/examen_rs_db
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

---

## 💻 Utilisation

### Interface GraphiQL

Accédez à l'interface GraphiQL pour tester les requêtes GraphQL :
```
http://localhost:8080/graphiql
```

### Exemples de requêtes

#### REST API (avec cURL)

**Créer une personne :**
```bash
curl -X POST http://localhost:8080/api/personnes \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Diop",
    "prenom": "Moussa",
    "dateNaissance": "1995-03-15",
    "adresse": "Dakar, Sénégal",
    "telephone": "771234567"
  }'
```

**Modifier une personne :**
```bash
curl -X PUT http://localhost:8080/api/personnes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Fall",
    "prenom": "Fatou",
    "dateNaissance": "1998-06-20",
    "adresse": "Thiès, Sénégal",
    "telephone": "779876543"
  }'
```

**Supprimer une personne :**
```bash
curl -X DELETE http://localhost:8080/api/personnes/1
```

#### GraphQL API

**Lister toutes les personnes :**
```graphql
query {
  allPersonnes {
    id
    nom
    prenom
    dateNaissance
    adresse
    telephone
  }
}
```

**Rechercher par nom :**
```graphql
query {
  searchPersonnes(nom: "Diop") {
    id
    nom
    prenom
    telephone
  }
}
```

**Récupérer une personne par ID :**
```graphql
query {
  personneById(id: 1) {
    id
    nom
    prenom
    dateNaissance
  }
}
```

---

## 🏗️ Architecture

### Architecture en couches
```
┌─────────────────────────────────────┐
│         Client (React)              │
└──────────────┬──────────────────────┘
               │ HTTP/GraphQL
┌──────────────▼──────────────────────┐
│    Controller Layer                 │
│  - PersonneRestController           │
│  - PersonneGraphQLController        │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│    Service Layer                    │
│  - PersonneServiceImpl              │
│  - Validation métier                │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│    Repository Layer                 │
│  - PersonneRepository (JPA)         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│    Database (MariaDB)               │
└─────────────────────────────────────┘
```

### Structure du projet
```
src/main/java/com/leserviteurs/backend_rest_grapql/
├── model/
│   └── Personne.java                    # Entité JPA
├── repository/
│   └── PersonneRepository.java          # Interface JPA
├── dto/
│   └── PersonneDTO.java                 # Data Transfer Object
├── mapper/
│   └── PersonneMapper.java              # Conversion Entity ↔ DTO
├── service/
│   ├── PersonneService.java             # Interface Service
│   └── PersonneServiceImpl.java         # Implémentation
├── controller/
│   └── PersonneRestController.java      # API REST
├── graphql/
│   ├── PersonneGraphQLController.java   # API GraphQL
│   └── GraphQLExceptionHandler.java     # Gestion erreurs GraphQL
├── exception/
│   ├── ResourceNotFoundException.java   # Exception personnalisée
│   ├── GlobalExceptionHandler.java      # Gestion erreurs REST
│   └── ErrorResponse.java               # Format erreurs
├── config/
│    └── CorsConfig.java                  # Configuration CORS
├── validation                            
    └── ValidationUtils.java              # Validations des champs

src/main/resources/
├── application.properties               # Configuration
└── graphql/
    └── schema.graphqls                  # Schéma GraphQL
```

---

## 📚 API Documentation

### REST Endpoints

| Méthode | Endpoint | Description | Statut |
|---------|----------|-------------|--------|
| POST | `/api/personnes` | Créer une personne | 201 Created |
| PUT | `/api/personnes/{id}` | Modifier une personne | 200 OK |
| DELETE | `/api/personnes/{id}` | Supprimer une personne | 204 No Content |
| DELETE | `/api/personnes/reset` | Réinitialiser la table (dev) | 200 OK |

### GraphQL Queries

#### `allPersonnes`
Récupère toutes les personnes.

**Exemple :**
```graphql
query {
  allPersonnes {
    id
    nom
    prenom
  }
}
```

#### `personneById(id: ID!)`
Récupère une personne par son ID.

**Exemple :**
```graphql
query {
  personneById(id: 1) {
    nom
    prenom
    telephone
  }
}
```

#### `searchPersonnes(nom, prenom, telephone)`
Recherche des personnes avec filtres optionnels.

**Exemple :**
```graphql
query {
  searchPersonnes(nom: "Diop", prenom: "Moussa") {
    id
    nom
    prenom
  }
}
```

### Codes de statut HTTP

| Code | Signification | Cas d'usage |
|------|---------------|-------------|
| 200 | OK | Modification réussie |
| 201 | Created | Création réussie |
| 204 | No Content | Suppression réussie |
| 400 | Bad Request | Validation échouée |
| 404 | Not Found | Ressource inexistante |
| 500 | Internal Server Error | Erreur serveur |

---

## 🧪 Tests

### Tester avec Postman

1. Importer la collection : `tests/postman-collection.json`
2. Exécuter les requêtes

### Tester avec GraphiQL

1. Ouvrir http://localhost:8080/graphiql
2. Copier-coller les requêtes du fichier `tests/graphql-queries.txt`

### Tests unitaires
```bash
mvn test
```

### Vérifier la base de données
```sql
USE examen_rs_db;
SELECT * FROM personne;
```

---

## 🔧 Validation et Règles Métier

### Validations automatiques

- **Nom** : Obligatoire, max 100 caractères → Normalisé en MAJUSCULES(ne contient que des lettres et espace)
- **Prénom** : Obligatoire, max 100 caractères → Première lettre en majuscule(ne contient que des lettres et espace)
- **Date de naissance** : Doit être dans le passé
- **Téléphone** : Unique, max 20 caractères → Espaces supprimés(en pratique la limte est 9 caractères pour le Sénégal et seulement des chiffres commencçant par 7. parexemple 77, 76, 70,78, 75)
- **Adresse** : Max 255 caractères → Cretaines caractères sont autorisé et d'autres non

### Exemple de normalisation

**Entrée :**
```json
{
  "nom": "diop",
  "prenom": "moussa"
}
```

**Sortie :**
```json
{
  "nom": "DIOP",
  "prenom": "Moussa"
}
```

---

## 🐛 Gestion des erreurs

### Erreurs REST

**Exemple : Validation échouée**
```json
{
  "timestamp": "2025-01-28T14:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "nom": "Le nom est obligatoire",
    "dateNaissance": "La date de naissance doit être dans le passé"
  }
}
```

**Exemple : Ressource non trouvée**
```json
{
  "timestamp": "2025-01-28T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Personne non trouvée avec l'ID : 999"
}
```

### Erreurs GraphQL

**Exemple : ID inexistant**
```json
{
  "errors": [
    {
      "message": "Personne non trouvée avec l'ID : 999",
      "extensions": {
        "classification": "NOT_FOUND"
      }
    }
  ],
  "data": {
    "personneById": null
  }
}
```

---

## 📊 Fonctionnalités avancées

- ✅ **Injection par constructeur** (meilleures pratiques Spring)
- ✅ **Logs structurés** avec SLF4J
- ✅ **Gestion d'exceptions** centralisée (REST et GraphQL)
- ✅ **CORS** configuré pour le frontend
- ✅ **Validation métier** personnalisée
- ✅ **DTO Pattern** pour séparer modèle et présentation
- ✅ **Transactions** automatiques avec `@Transactional`

---

## 📄 License

Ce projet est développé dans le cadre d'un examen académique.

---

## 👨‍💻 Auteur

**Aliou DIOP**
- GitHub: [AliouServiteurs](https://github.com/AliouServiteurs)
- Email: alioudiop463@gmail.com

---

## 🙏 Remerciements

- Spring Boot Documentation
- GraphQL Java Documentation
- Baeldung Tutorials

---

## 📝 Notes de développement

### Commandes utiles
```bash
# Démarrer l'application
mvn spring-boot:run

# Compiler
mvn clean install

# Build sans tests
mvn clean install -DskipTests

# Créer un JAR
mvn package

# Lancer les tests
mvn test
```

### Endpoints de développement

- **Application** : http://localhost:8080
- **GraphiQL** : http://localhost:8080/graphiql
- **API REST** : http://localhost:8080/api/personnes

---

## 🔮 Améliorations futures

- [ ] Pagination pour `allPersonnes`
- [ ] Authentification JWT
- [ ] Documentation Swagger/OpenAPI
- [ ] Tests unitaires et d'intégration
- [ ] Docker containerization
- [ ] CI/CD avec GitHub Actions
- [ ] Frontend React complet

---

## 📞 Support

Pour toute question ou problème, ouvrez une [issue](https://github.com/votre-username/backend-rest-graphql/issues).

---

**⭐ Si ce projet vous aide, n'hésitez pas à mettre une étoile !**
