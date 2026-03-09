# Mini ERP - Projet Pédagogique Java EE (Jakarta EE)

Ce projet est une application web modulaire de gestion commerciale (Mini-ERP) développée en **Java (Jakarta EE)**. L'objectif est de démontrer une architecture en couches (Contrôleurs, Services, DAO, Modèles) sans l'utilisation de frameworks lourds comme Spring.

## 🛠️ Stack Technique
- **Backend** : Java 17+, Jakarta EE (Servlets, JSP, JSTL, Filtres)
- **Persistance** : Hibernate (JPA), Base de données PostgreSQL
- **Frontend** : HTML5, Bootstrap 5 (via CDN)
- **Serveur d'application** : Apache Tomcat 10+
- **Build & Dépendances** : Maven

---

## 📚 Documentation Technique par Module

Afin de documenter précisément les choix d'architecture et les règles de gestion, la conception technique a été découpée par module dans le dossier `docs/` :

1. 🔐 **[Authentification & Initialisation](docs/AUTH-CONCEPTION.md)**
   - Déploiement initial (Setup du premier administrateur).
   - Connexion, déconnexion et protection par session.

2. 👥 **[Gestion des Employés & Rôles](docs/USERS-ROLES-CONCEPTION.md)**
   - Gestion des accès : Administrateur (`ADMIN`), Magasinier (`STOCK`), Commercial (`COMMERCIAL`).
   - Mécanisme d'autorisation (RBAC) via Filtre Servlet.

3. 🏢 **[Gestion des Clients](docs/CLIENTS-CONCEPTION.md)**
   - Entité Client.
   - Interface CRUD pour les commerciaux.

4. 📦 **[Gestion du Catalogue Produits](docs/PRODUCTS-CONCEPTION.md)**
   - Entité Produit (Référence, Stock, Prix).
   - Upload et gestion d'images (multipart/form-data).

5. 🛒 **[Commandes & Facturation](docs/ORDERS-INVOICES-CONCEPTION.md)**
   - Création de paniers (Lignes de commande multi-produits).
   - Génération de factures (Principe d'immuabilité et snapshot).
   - Vues adaptées pour l'impression PDF.

---

## 🚀 Lancement Rapide

1. Assurez-vous d'avoir une instance **PostgreSQL** démarrée avec une base de données nommée `mini_erp`.
   - *Modifiez les identifiants dans `src/main/resources/META-INF/persistence.xml` si nécessaire.*
2. Compilez le projet via Maven : `mvn clean install`
3. Déployez le fichier `.war` généré (dans `/target/mini-erp.war`) sur un serveur **Tomcat 10**.
4. Au premier lancement, les tables seront générées automatiquement (grâce à `hbm2ddl.auto=update`).
5. Accédez à l'URL `http://localhost:8080/mini-erp` : vous serez invité à configurer le **premier compte Administrateur**.
