<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
  if (session == null || session.getAttribute("user") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
  }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tableau de bord - Mini ERP</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          crossorigin="anonymous">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-semibold" href="#">
            Mini ERP
        </a>

        <div class="d-flex align-items-center ms-auto">
            <span class="text-white-50 me-3">
                Connecté en tant que <strong>${sessionScope.user.username}</strong>
                (<span class="text-uppercase">${sessionScope.user.role}</span>)
            </span>
            <a class="btn btn-outline-light btn-sm"
               href="${pageContext.request.contextPath}/logout">
                Déconnexion
            </a>
        </div>
    </div>
</nav>

<main class="container py-4">
    <div class="row">
        <div class="col-12">
            <h1 class="h3 mb-3">Tableau de bord</h1>
            <c:if test="${param.access == 'denied'}">
                <div class="alert alert-warning mb-3" role="alert">
                    Vous n'avez pas les droits d'accès à cette section.
                </div>
            </c:if>
            <p class="text-muted">
                Bienvenue dans votre mini ERP. Accédez aux modules selon votre rôle.
            </p>
        </div>
    </div>

    <div class="row g-3 mt-1">
        <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'COMMERCIAL'}">
            <div class="col-md-6 col-lg-3">
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <h2 class="h6 mb-2">Clients</h2>
                        <p class="text-muted small mb-3">Gérer les fiches clients.</p>
                        <a class="btn btn-outline-primary btn-sm"
                           href="${pageContext.request.contextPath}/clients">
                            Voir les clients
                        </a>
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'STOCK'}">
            <div class="col-md-6 col-lg-3">
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <h2 class="h6 mb-2">Produits</h2>
                        <p class="text-muted small mb-3">Catalogue produits et stock.</p>
                        <a class="btn btn-outline-primary btn-sm"
                           href="${pageContext.request.contextPath}/products">
                            Voir les produits
                        </a>
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <div class="col-md-6 col-lg-3">
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <h2 class="h6 mb-2">Employés</h2>
                        <p class="text-muted small mb-3">Gérer les comptes (commercial, stock).</p>
                        <a class="btn btn-outline-primary btn-sm"
                           href="${pageContext.request.contextPath}/users">
                            Voir les employés
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-6 col-lg-3">
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <h2 class="h6 mb-2">Commandes</h2>
                        <p class="text-muted small mb-3">Créer et suivre les commandes.</p>
                        <a class="btn btn-outline-primary btn-sm"
                           href="${pageContext.request.contextPath}/orders">
                            Voir les commandes
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-6 col-lg-3">
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <h2 class="h6 mb-2">Factures</h2>
                        <p class="text-muted small mb-3">Facturation et règlements.</p>
                        <button class="btn btn-outline-primary btn-sm" disabled>
                            Bientôt disponible
                        </button>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</main>

<!-- Bootstrap 5 JS bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
</body>
</html>

