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
    <title>Commandes - Mini ERP</title>

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
            <h1 class="h3 mb-0">Commandes</h1>
            <p class="text-muted mb-0">Liste des commandes</p>
        </div>
        <a href="${pageContext.request.contextPath}/orders/new"
           class="btn btn-primary btn-sm">
            Nouvelle commande
        </a>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty orders}">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0 align-middle">
                            <thead class="table-light">
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Client</th>
                                <th scope="col">Date</th>
                                <th scope="col">Statut</th>
                                <th scope="col" class="text-end">Total</th>
                                <th scope="col" class="text-end">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <th scope="row">${order.id}</th>
                                    <td>${order.client.name}</td>
                                    <td>${order.orderDate}</td>
                                    <td><span class="badge bg-secondary">${order.status}</span></td>
                                    <td class="text-end">${order.totalAmount} €</td>
                                    <td class="text-end">
                                        <form action="${pageContext.request.contextPath}/orders/delete"
                                              method="post"
                                              class="d-inline"
                                              onsubmit="return confirm('Supprimer cette commande ?');">
                                            <input type="hidden" name="id" value="${order.id}"/>
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
                        Aucune commande. <a href="${pageContext.request.contextPath}/orders/new">Créer une commande</a>.
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
