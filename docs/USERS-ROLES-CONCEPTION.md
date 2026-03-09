# Conception Technique : Gestion des Employés et des Rôles

Ce document détaille la gestion interne des comptes utilisateurs (employés) et le système d'autorisations (rôles) au sein du Mini ERP.
*(Pour le mécanisme de login et de setup initial, se référer à `AUTH-CONCEPTION.md`).*

## 1. Objectif et Périmètre

- **Gestion des employés** : Permettre à l'Administrateur de créer, modifier et supprimer des comptes pour les employés de l'entreprise.
- **Système de rôles** : Assigner des permissions spécifiques à chaque compte selon sa fonction dans l'entreprise (Admin, Magasinier, Commercial).

---

## 2. Définition des Rôles

Les rôles déterminent les fonctionnalités accessibles via l'application. La classe utilitaire **`Role.java`** centralise cette configuration.

- **`ADMIN`** : Accès complet à toutes les fonctionnalités (Tableau de bord, Clients, Produits, Employés, Commandes, Factures).
- **`STOCK`** (Gestionnaire de stock) : Accès restreint à la gestion du catalogue **Produits** (lecture/écriture).
- **`COMMERCIAL`** (Vendeur / Chargé de clientèle) : Accès à la gestion des **Clients** (lecture/écriture), des **Commandes** et des **Factures**.

La classe `Role.java` expose des méthodes statiques (ex: `canAccessClients(role)`) utilisées par l'`AuthFilter`.

---

## 3. Modèle de Données

L'entité `User` (`users`) stocke les identifiants et le rôle.
- `id` (Long, PK)
- `username` (String, unique, non nul)
- `password` (String, non nul) - *Stocké en clair dans cette version pédagogique.*
- `role` (String, non nul) - `ADMIN`, `STOCK`, ou `COMMERCIAL`.

---

## 4. Couche Service : UserManagementService

Pour distinguer l'authentification de l'administration des comptes, la classe **`UserManagementService`** a été créée.

**Règles métier de validation :**
1. **Unicité de l'identifiant** : Il est impossible de créer ou de renommer un utilisateur avec un `username` déjà pris par un autre compte.
2. **Protection de soi-même** : Un administrateur ne peut **pas supprimer son propre compte** pour éviter le blocage du système.
3. **Contrôle des rôles** : Le rôle affecté doit faire partie des constantes définies (`ADMIN`, `STOCK`, `COMMERCIAL`).

---

## 5. Contrôleurs et Vues

L'administrateur dispose d'une interface CRUD complète pour gérer les employés :

- **Servlets** : `UserListServlet`, `UserFormServlet`, `UserEditServlet`, `UserDeleteServlet`.
- **Vues** : 
  - `users.jsp` (liste des employés)
  - `user-form.jsp` (formulaire de création / édition avec liste déroulante pour le choix du rôle).
  
**Détail technique JSP :**
Dans le formulaire `user-form.jsp`, afin d'éviter les conflits avec l'objet de session `${user}` (l'utilisateur actuellement connecté), l'objet transféré par la servlet pour l'édition a été nommé `${formUser}`. 

---

## 6. L'AuthFilter (Contrôle d'accès)

Le **`AuthFilter`** agit comme la barrière de sécurité centrale de l'application :
1. **Authentification** : Vérifie qu'il y a un utilisateur en session, sinon redirection `/login`.
2. **Autorisation (RBAC)** : Analyse le `ServletPath` de la requête HTTP :
   - Requête vers `/clients/*` : Autorise si `Role.canAccessClients()` = `true`.
   - Requête vers `/products/*` : Autorise si `Role.canAccessProducts()` = `true`.
   - Requête vers `/users/*` : Autorise si `Role.canAccessUserManagement()` = `true` (uniquement ADMIN).
   - Requête vers `/orders/*` ou `/invoices/*` : Autorise selon les règles correspondantes.
   - En cas d'accès refusé, redirige silencieusement vers `/dashboard.jsp?access=denied`.
