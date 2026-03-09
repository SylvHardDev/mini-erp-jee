# Conception Technique : Gestion des Clients

Ce document décrit l'architecture et les choix techniques relatifs au module de **Gestion des Clients** dans le Mini ERP.

## 1. Objectif et Périmètre

Le module **Clients** permet de gérer le carnet d'adresses des acheteurs (entreprises ou particuliers).
Ces clients seront ultérieurement utilisés pour la création de commandes et la facturation.
Les fonctionnalités incluses sont :
- **Lister** tous les clients.
- **Ajouter** un nouveau client.
- **Modifier** un client existant.
- **Supprimer** un client.

---

## 2. Modèle de Données (Entité JPA)

L'entité `Client` est mappée sur la table `clients` via JPA.

**Attributs :**
- `id` (Long, PK, auto-incrément)
- `name` (String, non nul, longueur 100) : Nom ou raison sociale.
- `email` (String, non nul, unique, longueur 150) : Adresse email de contact.
- `phone` (String, longueur 30) : Numéro de téléphone.
- `address` (String, longueur 255) : Adresse postale.
- `city` (String, longueur 100) : Ville.
- `country` (String, longueur 20) : Pays.

---

## 3. Architecture et Couches

Le module suit l'architecture classique en couches :

### 3.1. Couche d'Accès aux Données (DAO)
**`ClientDAO`** : encapsule les opérations de persistance via l'`EntityManager` (JPA).
- `findAll()` : Retourne la liste des clients triée par nom (`ORDER BY c.name`).
- `findById(Long id)` : Récupère un client par son ID.
- `save(Client)`, `update(Client)`, `delete(Long id)` : Méthodes transactionnelles de modification de la base.

### 3.2. Couche Service (Métier)
**`ClientService`** : contient la logique métier et fait l'interface entre les contrôleurs et le DAO.
- **Validation** (`validate(Client)`) : Vérifie que le nom et l'email sont renseignés. Vérifie de façon basique le format de l'email (présence de `@` et `.`). Lève une `IllegalArgumentException` en cas d'erreur.

### 3.3. Contrôleurs (Servlets)
- **`ClientListServlet` (`/clients`)** : Récupère la liste des clients et redirige vers `clients.jsp`.
- **`ClientFormServlet` (`/clients/new`)** : 
  - GET : Affiche le formulaire vide (`client-form.jsp`).
  - POST : Récupère les paramètres, crée l'objet `Client`, tente de l'enregistrer via `ClientService`. En cas d'erreur (ex: email invalide), réaffiche le formulaire avec un message d'erreur.
- **`ClientEditServlet` (`/clients/edit`)** :
  - GET : Charge les données du client existant et pré-remplit `client-form.jsp`.
  - POST : Met à jour les informations du client.
- **`ClientDeleteServlet` (`/clients/delete`)** : 
  - POST : Supprime le client selon son ID et redirige vers la liste.

### 3.4. Vues (JSP)
- **`clients.jsp`** : Tableau listant les clients. Actions "Modifier" et "Supprimer".
- **`client-form.jsp`** : Formulaire mutualisé pour la création et la modification (utilisation de JSTL `<c:choose>`).

---

## 4. Sécurité et Rôles

L'accès à la gestion des clients est protégé :
- **Rôles autorisés** : `ADMIN` et `COMMERCIAL` (défini dans `Role.canAccessClients(role)`).
- **Filtre** : `AuthFilter` vérifie que l'utilisateur a l'un de ces rôles avant d'autoriser l'accès aux URLs `/clients*` et `/client-form.jsp`.
- **Dashboard** : La carte d'accès au module "Clients" n'est visible que pour les profils `ADMIN` et `COMMERCIAL`.
