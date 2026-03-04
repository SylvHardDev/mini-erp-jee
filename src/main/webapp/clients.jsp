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
    <title>Clients - Mini ERP</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          crossorigin="anonymous">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-semibold" href="${pageContext.request.contextPath}/dashboard.jsp">
            Mini ERP
        </a>

        <div class="d-flex align-items-center ms-auto">
            <span class="text-white-50 me-3">
                <strong>${sessionScope.user.username}</strong>
            </span>
            <a class="btn btn-outline-light btn-sm"
               href="${pageContext.request.contextPath}/logout">
                Déconnexion
            </a>
        </div>
    </div>
</nav>

<main class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
            <h1 class="h3 mb-0">Clients</h1>
            <p class="text-muted mb-0">Liste de tous les clients</p>
        </div>
        <a href="${pageContext.request.contextPath}/clients/new"
           class="btn btn-primary btn-sm">
            Nouveau client
        </a>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty clients}">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0 align-middle">
                            <thead class="table-light">
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Nom</th>
                                <th scope="col">Email</th>
                                <th scope="col">Téléphone</th>
                                <th scope="col">Ville</th>
                                <th scope="col">Pays</th>
                                <th scope="col" class="text-end">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="client" items="${clients}">
                                <tr>
                                    <th scope="row">${client.id}</th>
                                    <td>${client.name}</td>
                                    <td>${client.email}</td>
                                    <td>${client.phone}</td>
                                    <td>${client.city}</td>
                                    <td>${client.country}</td>
                                    <td class="text-end">
                                        <a class="btn btn-sm btn-outline-secondary me-1"
                                           href="${pageContext.request.contextPath}/clients/edit?id=${client.id}">
                                            Modifier
                                        </a>
                                        <form action="${pageContext.request.contextPath}/clients/delete"
                                              method="post"
                                              class="d-inline"
                                              onsubmit="return confirm('Supprimer ce client ?');">
                                            <input type="hidden" name="id" value="${client.id}"/>
                                            <button type="submit" class="btn btn-sm btn-outline-danger">
                                                Supprimer
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="p-4 text-center text-muted">
                        Aucun client pour le moment. Nous ajouterons bientôt un formulaire
                        de création.
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
</body>
</html>

