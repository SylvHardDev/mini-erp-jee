# Conception Technique : Commandes et Factures

Ce document décrit l'architecture et les choix techniques relatifs aux modules de **Commandes** (Orders) et de **Factures** (Invoices) implémentés dans le Mini ERP.

## 1. Règles Métier

### 1.1. Commandes (Orders)
- Une commande est associée à un **Client** (obligatoire).
- Elle contient au moins une **Ligne de commande** (produit, quantité, prix unitaire).
- Le prix unitaire d'une ligne de commande est figé au moment de la création (copie du prix du produit) pour éviter que les modifications futures du catalogue n'altèrent l'historique.
- Une commande possède une date (`LocalDate`) et un statut (par défaut `DRAFT`).

### 1.2. Factures (Invoices)
- Une facture est **générée à partir d'une commande**. Une commande ne peut être facturée qu'une seule fois.
- **Principe d'immuabilité (Snapshot)** : lors de la facturation, les informations du client (nom, email, adresse) et des produits (référence, libellé, prix unitaire, quantité) sont recopiées dans la facture. Si le client est supprimé ou le produit modifié ultérieurement, la facture reste intacte sur le plan comptable.
- Numérotation automatique et séquentielle par année (ex. `FAC-2025-0001`, `FAC-2025-0002`).
- La facture est liée techniquement à la commande source, mais ses données lui sont propres.

---

## 2. Modèle de Données (Entités JPA)

Les entités sont déclarées dans `persistence.xml` et générées via Hibernate (`hbm2ddl.auto=update`).

### 2.1. Order et OrderLine
- **`Order`** (`orders`)
  - `id` (PK, auto-incrément)
  - `client` (ManyToOne, FK `client_id`, lazy fetch)
  - `orderDate` (LocalDate)
  - `status` (String)
  - `lines` (OneToMany, mappé par `order`, cascade ALL, orphanRemoval)
  - Méthode `getTotalAmount()` : somme du total de chaque ligne.

- **`OrderLine`** (`order_lines`)
  - `id` (PK)
  - `order` (ManyToOne)
  - `product` (ManyToOne, FK `product_id`)
  - `quantity` (Integer)
  - `unitPrice` (BigDecimal, prix figé à la commande)
  - Méthode `getLineTotal()` : `quantity * unitPrice`.

### 2.2. Invoice et InvoiceLine
- **`Invoice`** (`invoices`)
  - `id` (PK)
  - `invoiceNumber` (String, unique, ex: "FAC-2025-0001")
  - `invoiceDate` (LocalDate)
  - `order` (ManyToOne, FK `order_id`)
  - `status` (String)
  - Champs snapshot client : `clientName`, `clientEmail`, `clientAddress`
  - `lines` (OneToMany, cascade ALL)

- **`InvoiceLine`** (`invoice_lines`)
  - `id` (PK)
  - `invoice` (ManyToOne)
  - Champs snapshot produit : `productReference`, `productLabel`
  - `quantity` (Integer)
  - `unitPrice` (BigDecimal)

---

## 3. Couche d'Accès aux Données (DAO)

- **`OrderDAO`**
  - `findAll()` : requête HQL avec `LEFT JOIN FETCH` sur le client et les lignes pour éviter le problème "N+1 selects".
  - `findById(id)` : fetch complet incluant le client, les lignes et les produits des lignes.
  - `save()`, `delete()` : gestion standard des transactions via l'`EntityManager`.

- **`InvoiceDAO`**
  - `findAll()`, `findById()` (fetch des lignes et de la commande associée).
  - `findByOrderId(orderId)` : permet de vérifier si une commande a déjà été facturée.
  - `countByNumberPrefix(prefix)` : compte le nombre de factures d'une année donnée pour générer le numéro suivant.

---

## 4. Couche Service

### 4.1. OrderService
- **Validation** : vérifie la présence du client, la date, et l'existence d'au moins une ligne valide (quantité > 0 et produit sélectionné).
- **`buildOrderFromParams(clientId, productIds[], quantities[])`** : méthode métier pour construire l'objet `Order` et ses `OrderLine` à partir des tableaux de chaînes envoyés par le formulaire HTML (post). Aligne les index et filtre les lignes vides ou avec quantité ≤ 0.

### 4.2. InvoiceService
- **`createFromOrder(orderId)`** : logique de facturation.
  1. Récupère la commande via `OrderService`.
  2. Vérifie qu'il n'existe pas déjà de facture pour cet `orderId`.
  3. Appelle `generateNextInvoiceNumber()` : construit le préfixe (ex. `FAC-2025-`) et compte les existantes.
  4. Crée la facture et copie les données du client.
  5. Itère sur les `OrderLine` pour créer les `InvoiceLine` (copie de la référence, libellé, prix et quantité).
  6. Sauvegarde la facture via `InvoiceDAO`.

---

## 5. Contrôleurs (Servlets) et Vues (JSP)

### 5.1. Commandes
- **`OrderListServlet` (`/orders`)**
  - Récupère la liste des commandes.
  - Construit une Map `orderId -> invoiceId` pour savoir si chaque commande est facturée.
  - Vue : `orders.jsp` (Tableau. Si facturée : bouton "Voir facture", sinon : bouton "Facturer").
- **`OrderFormServlet` (`/orders/new`)**
  - GET : charge la liste des clients et produits, affiche `order-form.jsp`.
  - POST : lit `clientId`, `productId[]`, `quantity[]`, demande au service de construire et de sauvegarder la commande.
  - Vue : `order-form.jsp` (JavaScript basique pour cloner la première ligne du tableau de produits).
- **`OrderDeleteServlet` (`/orders/delete`)** (POST).

### 5.2. Factures
- **`InvoiceListServlet` (`/invoices`)** : Liste des factures générées (`invoices.jsp`).
- **`InvoiceGenerateServlet` (`/invoices/generate`)**
  - Déclenché via POST depuis la liste des commandes (clic sur "Facturer").
  - Appelle `createFromOrder` et redirige vers la vue de la facture.
- **`InvoiceViewServlet` (`/invoices/view?id=...`)**
  - Charge la facture et l'affiche sur `invoice-view.jsp`.
  - Vue `invoice-view.jsp` : mise en page épurée (style "papier"). Inclut un bouton "Imprimer / PDF" qui appelle `window.print()`. Une règle CSS `@media print` masque la barre de navigation.

---

## 6. Sécurité et Rôles

Le module est soumis au contrôle d'accès défini dans **`Role.java`** et **`AuthFilter.java`**.

- **Droits d'accès** :
  - `canAccessOrders(role)` : retourne `true` pour `ADMIN` et `COMMERCIAL`.
  - `canAccessInvoices(role)` : retourne `true` pour `ADMIN` et `COMMERCIAL`.
- **Filtre** : Intercepte les URLs `/orders*`, `/order-form.jsp`, `/invoices*`, `/invoice-view.jsp` et redirige vers `/dashboard.jsp?access=denied` si le rôle de l'utilisateur n'est pas autorisé.
- **Dashboard** : Les cartes "Commandes" et "Factures" s'affichent dynamiquement (condition JSP `ADMIN || COMMERCIAL`) au lieu du bouton "Bientôt disponible".
