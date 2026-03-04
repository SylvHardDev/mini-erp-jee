# Prochaine étape : gestion des rôles

## Rappel : à quoi sert le module **Clients** ?

Dans le mini-ERP, les **clients** représentent les **acheteurs** (entreprises ou particuliers) à qui tu vendras des produits :

- **But** : fiches clients (raison sociale / nom, email, téléphone, adresse, ville, pays) pour les **commandes** et la **facturation** à venir.
- **Flux métier** : Client → Commande (panier de produits) → Facture.
- Donc : **Clients** = qui achète ; **Produits** = ce qu’on vend ; **Commandes** = lien client + lignes de produits ; **Factures** = document de vente.

Le CRUD Clients sert à créer / modifier / supprimer ces fiches avant de les utiliser dans les commandes.

---

## Rôles proposés

On peut s’appuyer sur le champ `User.role` déjà en base et définir **3 rôles** :

| Rôle | Rôle technique | Accès prévu |
|------|----------------|-------------|
| **Administrateur** | `ADMIN` | Tout : dashboard, clients (CRUD), produits (CRUD), plus tard commandes et factures. Peut créer des utilisateurs / gérer les rôles si on l’ajoute. |
| **Gestionnaire de stock** | `STOCK` | **Produits uniquement** : liste, création, modification (et éventuellement suppression) des produits. Pas d’accès à la gestion clients. |
| **Commercial / Gestion clients** | `COMMERCIAL` | **Clients** : liste, création, modification des clients. Peut-être consultation des produits (lecture seule) pour préparer les commandes plus tard. Pas de modification des produits ni de la partie stock. |

Tu peux ajuster (par ex. STOCK en lecture seule pour COMMERCIAL, ou suppression produits réservée à ADMIN).

---

## Prochaine étape technique recommandée

1. **Définir les rôles en code**  
   - Utiliser des constantes (ou un enum) : `ADMIN`, `STOCK`, `COMMERCIAL` (et adapter le setup pour créer un premier admin avec le rôle `ADMIN` si ce n’est pas déjà le cas).

2. **Contrôle d’accès par URL (filtre)**  
   - Après vérification de la session (AuthFilter), vérifier le **rôle** selon l’URL :
     - `/products/*` et `/product-image` : autorisés pour `ADMIN` et `STOCK`.
     - `/clients/*` : autorisés pour `ADMIN` et `COMMERCIAL`.
     - `/dashboard.jsp` : tous les rôles connectés.
   - Si le rôle n’est pas autorisé → redirection vers le dashboard (ou page « Accès refusé ») avec un message.

3. **Dashboard selon le rôle**  
   - Afficher seulement les cartes / liens correspondant au rôle :
     - **ADMIN** : Clients + Produits (et plus tard Commandes, Factures).
     - **STOCK** : uniquement Produits.
     - **COMMERCIAL** : uniquement Clients (et éventuellement « Catalogue produits » en lecture seule si tu l’ajoutes).

4. **Optionnel**  
   - Page ou formulaire (réservé ADMIN) pour créer des utilisateurs avec un rôle (STOCK, COMMERCIAL, ADMIN).  
   - Sinon, en attendant, tu peux créer les utilisateurs en base à la main avec le bon `role`.

---

## Résumé

- **Clients** = fiches des acheteurs, pour les commandes et factures à venir.
- **Prochaine étape** = mettre en place la **gestion des rôles** : ADMIN / STOCK (gestionnaire de stock) / COMMERCIAL, avec un filtre par URL et un dashboard adapté à chaque rôle.

---

## Tester les rôles (STOCK, COMMERCIAL)

Le premier compte créé via `/setup` a le rôle **ADMIN**. Pour tester **STOCK** et **COMMERCIAL**, crée des utilisateurs en base avec le bon rôle. Exemple en SQL (mot de passe en clair, à usage de test uniquement) :

```sql
-- Exemple : utilisateur "stock" / "stock" avec accès produits uniquement
INSERT INTO users (username, password, role) VALUES ('stock', 'stock', 'STOCK');

-- Exemple : utilisateur "commercial" / "commercial" avec accès clients uniquement
INSERT INTO users (username, password, role) VALUES ('commercial', 'commercial', 'COMMERCIAL');
```

Ensuite, connecte-toi avec ces identifiants et vérifie que le dashboard n’affiche que la carte autorisée, et qu’un accès direct à une URL non autorisée redirige vers le dashboard avec le message « Vous n'avez pas les droits d'accès à cette section ».
