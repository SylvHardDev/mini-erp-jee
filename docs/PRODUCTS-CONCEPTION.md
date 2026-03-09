# Conception Technique : Gestion des Produits

Ce document décrit l'architecture du module de **Gestion des Produits** dans le Mini ERP.

## 1. Objectif et Périmètre

Le module **Produits** sert à gérer le catalogue et le stock de l'entreprise. 
Les fonctionnalités incluses sont :
- **Lister** le catalogue des produits.
- **Ajouter** et **Modifier** un produit, avec gestion d'une **image associée** (upload de fichier).
- **Supprimer** un produit.

---

## 2. Modèle de Données (Entité JPA)

L'entité `Product` est mappée sur la table `products`.

**Attributs :**
- `id` (Long, PK, auto-incrément)
- `reference` (String, non nul, unique, longueur 50) : Référence unique (ex: PRD-001).
- `libelle` (String, non nul, longueur 200) : Nom du produit.
- `prixUnitaire` (BigDecimal, non nul) : Prix de vente.
- `stock` (Integer, non nul, défaut 0) : Quantité en stock.
- `description` (String, longueur 500) : Détails du produit.
- `imageData` (byte[], `@Lob`) : Données binaires de l'image du produit.
- `imageContentType` (String) : Type MIME de l'image (ex: `image/png`, `image/jpeg`).

---

## 3. Architecture et Couches

### 3.1. Couche d'Accès aux Données (DAO)
**`ProductDAO`** : Gère la persistance de l'entité `Product`.
- `findAll()` : Liste triée par référence.
- Opérations classiques `findById`, `save`, `update`, `delete`.

### 3.2. Couche Service (Métier)
**`ProductService`** : Applique les règles métier avant la persistance.
- **Validation** (`validate(Product)`) : 
  - Vérifie que la référence, le libellé et le prix sont renseignés.
  - Le prix et le stock ne peuvent pas être négatifs.
  - La **référence doit être unique**. Si une entité avec la même référence existe déjà (avec un ID différent), une exception est levée.

### 3.3. Contrôleurs (Servlets)
- **`ProductListServlet` (`/products`)** : Affiche la liste des produits.
- **`ProductFormServlet` (`/products/new`)** & **`ProductEditServlet` (`/products/edit`)** :
  - **`@MultipartConfig`** : Déclaré sur ces servlets pour permettre la lecture des données de formulaires d'upload de fichiers (`enctype="multipart/form-data"`).
  - Traitement de la `Part` "image" : lecture du fichier en `byte[]` et récupération du type MIME.
- **`ProductDeleteServlet` (`/products/delete`)** : Suppression du produit (POST).
- **`ProductImageServlet` (`/product-image?id=X`)** : 
  - Servlet dédiée pour **servir l'image** enregistrée en base de données. 
  - Elle configure le `Content-Type` de la réponse et écrit le `byte[]` dans l'`OutputStream` de la réponse HTTP.

### 3.4. Vues (JSP)
- **`products.jsp`** : Tableau présentant les produits, avec une balise `<img>` pointant vers `/product-image?id=${p.id}` (ou une image placeholder si absente).
- **`product-form.jsp`** : Formulaire utilisant `enctype="multipart/form-data"`. Un champ `<input type="file" name="image" accept="image/*">` permet la sélection de l'image.

---

## 4. Sécurité et Rôles

- **Rôles autorisés** : `ADMIN` et `STOCK` (défini dans `Role.canAccessProducts(role)`).
- **Filtre** : L'`AuthFilter` protège les URLs `/products*`, `/product-form.jsp` et `/product-image` pour s'assurer que seul le personnel concerné peut manipuler le catalogue.
- **Dashboard** : La carte "Produits" n'est affichée qu'aux rôles `ADMIN` et `STOCK`.
