# Conception technique – Authentification (Auth)

**Projet :** Mini ERP – Gestion commerciale  
**Module :** Authentification et premier déploiement  
**Stack :** Jakarta EE (Servlet, JSP, JPA), PostgreSQL, Tomcat 10  

---

## 1. Objectif et périmètre

La fonctionnalité **Auth** assure :

- La **création du premier compte administrateur** lorsque la base ne contient aucun utilisateur (première installation).
- L’**identification** des utilisateurs (login) et la **déconnexion** (logout).
- La **protection** des pages métier : seuls les utilisateurs ayant une session valide peuvent accéder au tableau de bord et aux écrans clients (et futurs modules).

Elle ne gère pas (pour l’instant) : gestion des rôles fine (ADMIN vs USER), hash des mots de passe, verrouillage de compte, « mot de passe oublié ».

---

## 2. Règles métier

### 2.1 Premier déploiement (setup)

| Règle | Description |
|-------|-------------|
| **RM-AUTH-01** | Si la table `users` est **vide**, toute tentative d’accès à la racine (`/`) ou à la page de login (`/login`) doit **rediriger** vers une interface dédiée de création du premier administrateur (`/setup`). |
| **RM-AUTH-02** | La page de setup n’est **accessible** que lorsqu’il n’existe **aucun** utilisateur. Dès qu’au moins un utilisateur existe, l’accès à `/setup` redirige vers `/login`. |
| **RM-AUTH-03** | Lors de la création du premier admin : **identifiant** et **mot de passe** sont obligatoires ; le **mot de passe** doit être **confirmé** (saisie identique). En cas d’erreur, le formulaire est réaffiché avec un message d’erreur et l’identifiant conservé. |
| **RM-AUTH-04** | Le premier utilisateur créé via `/setup` a le **rôle** `ADMIN`. |
| **RM-AUTH-05** | Après création réussie du compte admin, l’utilisateur est **redirigé** vers la page de login avec un message de succès invitant à se connecter. |

### 2.2 Connexion (login)

| Règle | Description |
|-------|-------------|
| **RM-AUTH-06** | L’authentification repose sur la saisie d’un **identifiant** (username) et d’un **mot de passe**. La vérification se fait par comparaison avec les données en base (mot de passe en clair pour l’instant). |
| **RM-AUTH-07** | Si les identifiants sont **valides**, un objet `User` est stocké en **session** sous l’attribut `"user"`, et l’utilisateur est **redirigé** vers le tableau de bord (`/dashboard.jsp`). |
| **RM-AUTH-08** | Si les identifiants sont **invalides**, la page de login est réaffichée avec un message d’erreur « Identifiants invalides », sans redirection. |
| **RM-AUTH-09** | Un utilisateur **déjà connecté** (session avec `"user"`) qui accède à `/login` est **redirigé** vers le tableau de bord, sans afficher le formulaire. |

### 2.3 Déconnexion (logout)

| Règle | Description |
|-------|-------------|
| **RM-AUTH-10** | La déconnexion **invalide** la session courante puis **redirige** vers la page de login. |

### 2.4 Protection des ressources

| Règle | Description |
|-------|-------------|
| **RM-AUTH-11** | Les URLs suivantes sont **protégées** : accès autorisé **uniquement** si un utilisateur est présent en session ; sinon **redirection** vers `/login`. Liste (non exhaustive, à aligner avec le code) : `/dashboard.jsp`, `/clients`, `/clients.jsp`, `/clients/new`, `/clients/edit`, `/clients/delete`, `/client-form.jsp`. |
| **RM-AUTH-12** | Les pages **publiques** (accessibles sans être connecté) sont : `/`, `/login`, `/login.jsp`, `/setup`, `/setup.jsp`, et la servlet `/logout` (GET). |

---

## 3. Architecture

### 3.1 Principe général

L’authentification suit l’**architecture en couches** du projet :

- **Présentation** : JSP (formulaires, messages) + Servlets (contrôle des requêtes, redirections).
- **Filtres** : exécutés avant les servlets/JSP pour redirection conditionnelle (setup ou login) et contrôle d’accès (AuthFilter).
- **Métier** : `AuthService` (règles d’authentification).
- **Accès aux données** : `UserDAO` (requêtes JPA).
- **Modèle** : entité JPA `User` et configuration (`JPAUtil`, `persistence.xml`).

### 3.2 Schéma des couches (Auth)

```
┌─────────────────────────────────────────────────────────────────────────┐
│  NAVIGATEUR                                                              │
└─────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  FILTRES (ordre d’exécution dépend du conteneur)                         │
│  • SetupRedirectFilter  : /, /login, /login.jsp → si 0 user → /setup     │
│  • AuthFilter           : URLs protégées → si pas de session → /login    │
└─────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  CONTRÔLEURS (Servlets)                                                  │
│  • SetupServlet   /setup     → premier admin (GET formulaire, POST cré.) │
│  • LoginServlet   /login     → connexion (GET formulaire, POST auth)    │
│  • LogoutServlet  /logout    → déconnexion (GET)                         │
└─────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  SERVICE MÉTIER                                                         │
│  • AuthService  → authenticate(login, password)                         │
└─────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  DAO                                                                     │
│  • UserDAO  → findByUsername(), hasAnyUser(), save()                      │
└─────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  JPA / BDD                                                               │
│  • User (entity)  ↔  table users (PostgreSQL)                             │
└─────────────────────────────────────────────────────────────────────────┘
```

### 3.3 Flux principaux

**Flux 1 – Premier accès (base vide)**  
`/` ou `/login` → **SetupRedirectFilter** (aucun user) → redirect `/setup` → **SetupServlet** GET → **setup.jsp** → utilisateur remplit le formulaire → POST `/setup` → **SetupServlet** POST (création User ADMIN) → redirect `/login?created=1` → **LoginServlet** GET → **login.jsp** (message succès).

**Flux 2 – Connexion**  
`/login` GET (avec users en base) → **SetupRedirectFilter** (hasAnyUser) → **LoginServlet** GET → **login.jsp**.  
Soumission formulaire → POST `/login` → **LoginServlet** POST → **AuthService.authenticate()** → **UserDAO.findByUsername()** → si OK : session.setAttribute("user"), redirect `/dashboard.jsp` ; sinon : forward **login.jsp** + message erreur.

**Flux 3 – Accès à une page protégée**  
Requête vers `/dashboard.jsp` (ou autre URL protégée) → **AuthFilter** : pas de `user` en session → redirect `/login`. Si session valide → **AuthFilter** laisse passer → ressource (JSP/servlet) s’exécute.

**Flux 4 – Déconnexion**  
GET `/logout` → **LogoutServlet** → session.invalidate() → redirect `/login`.

---

## 4. Fichiers impactés

### 4.1 Modèle et persistance

| Fichier | Rôle dans l’Auth |
|---------|-------------------|
| `src/main/java/com/mini/erp/model/User.java` | Entité JPA : id, username, password, role. Représente un utilisateur et est stockée en session après login. |
| `src/main/resources/META-INF/persistence.xml` | Unité de persistance `miniERP` : déclaration de l’entité `User`, connexion PostgreSQL. (Pas modifié spécifiquement pour Auth récemment.) |

### 4.2 Couche DAO

| Fichier | Rôle dans l’Auth |
|---------|-------------------|
| `src/main/java/com/mini/erp/dao/UserDAO.java` | `findByUsername(String)` : recherche d’un utilisateur pour le login. `hasAnyUser()` : indique si la base contient au moins un utilisateur (setup). `save(User)` : enregistrement du premier admin (et futurs utilisateurs). |

### 4.3 Couche service

| Fichier | Rôle dans l’Auth |
|---------|-------------------|
| `src/main/java/com/mini/erp/service/AuthService.java` | `authenticate(username, password)` : appelle `UserDAO.findByUsername()`, compare le mot de passe ; retourne l’entité `User` ou `null`. |

### 4.4 Contrôleurs (Servlets)

| Fichier | Rôle dans l’Auth |
|---------|-------------------|
| `src/main/java/com/mini/erp/controller/SetupServlet.java` | `/setup` : GET → affiche le formulaire de création du premier admin (si 0 user), sinon redirect `/login`. POST → validation (identifiant, mot de passe, confirmation), création User ADMIN, redirect `/login?created=1`. |
| `src/main/java/com/mini/erp/controller/LoginServlet.java` | `/login` : GET → si déjà connecté → redirect dashboard ; si `created=1` → message succès ; sinon affichage formulaire. POST → lecture username/password, appel `AuthService.authenticate()`, mise en session et redirect dashboard ou réaffichage login + erreur. |
| `src/main/java/com/mini/erp/controller/LogoutServlet.java` | `/logout` (GET) : invalidation de la session, redirect `/login`. |

### 4.5 Filtres

| Fichier | Rôle dans l’Auth |
|---------|-------------------|
| `src/main/java/com/mini/erp/filter/SetupRedirectFilter.java` | URLs : `"/"`, `"/login"`, `"/login.jsp"`. Si `!UserDAO.hasAnyUser()` → redirect `/setup` ; sinon laisse passer (vers login ou ressource). |
| `src/main/java/com/mini/erp/filter/AuthFilter.java` | URLs protégées : dashboard, clients (liste, formulaire, new, edit, delete). Vérifie la présence de `user` en session ; sinon redirect `/login`. |

### 4.6 Vues (JSP)

| Fichier | Rôle dans l’Auth |
|---------|-------------------|
| `src/main/webapp/setup.jsp` | Formulaire de création du premier admin : identifiant, mot de passe, confirmation. Messages d’erreur. Style Bootstrap. |
| `src/main/webapp/login.jsp` | Formulaire de connexion : identifiant, mot de passe. Affichage des attributs `error` et `success`. Style Bootstrap. |

### 4.7 Configuration et cycle de vie

| Fichier | Rôle dans l’Auth |
|---------|-------------------|
| `src/main/java/com/mini/erp/config/DataInitListener.java` | Listener : plus de création automatique d’admin ; fermeture de `JPAUtil` à l’arrêt de l’application. |
| `src/main/java/com/mini/erp/config/JPAUtil.java` | Fournit l’`EntityManager` / `EntityManagerFactory` utilisés par `UserDAO`. |
| `src/main/webapp/WEB-INF/web.xml` | Définition du welcome-file (ex. `login.jsp`) pour la racine du contexte. |

---

## 5. Synthèse

- **Règles métier** : premier admin via interface dédiée si base vide ; login/logout par session ; protection des URLs métier par filtre.
- **Architecture** : filtre(s) → servlets (setup, login, logout) → AuthService → UserDAO → JPA/User.
- **Fichiers impactés** : modèle `User`, `UserDAO`, `AuthService`, servlets Setup/Login/Logout, filtres SetupRedirect et Auth, JSP setup et login, listener et configuration JPA/web.

Ce document sert de **référence de conception et de documentation** pour la fonctionnalité Auth du Mini ERP.
