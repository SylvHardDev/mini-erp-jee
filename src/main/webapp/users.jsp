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
    <title>Employés - Mini ERP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet" crossorigin="anonymous">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-semibold" href="${pageContext.request.contextPath}/dashboard.jsp">Mini ERP</a>
        <div class="d-flex align-items-center ms-auto">
            <span class="text-white-50 me-3"><strong>${sessionScope.user.username}</strong></span>
            <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">Déconnexion</a>
        </div>
    </div>
</nav>

<main class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
            <h1 class="h3 mb-0">Employés</h1>
            <p class="text-muted mb-0">Gestion des comptes (commercial, stock, admin).</p>
        </div>
        <a href="${pageContext.request.contextPath}/users/new" class="btn btn-primary btn-sm">Nouvel employé</a>
    </div>

    <c:if test="${not empty sessionScope.userDeleteError}">
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            ${sessionScope.userDeleteError}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fermer"></button>
        </div>
        <c:remove var="userDeleteError" scope="session"/>
    </c:if>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty users}">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0 align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">Identifiant</th>
                                    <th scope="col">Rôle</th>
                                    <th scope="col" class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="u" items="${users}">
                                    <tr>
                                        <th scope="row">${u.id}</th>
                                        <td>${u.username}</td>
                                        <td><span class="badge bg-secondary">${u.role}</span></td>
                                        <td class="text-end">
                                            <a class="btn btn-sm btn-outline-secondary me-1"
                                               href="${pageContext.request.contextPath}/users/edit?id=${u.id}">Modifier</a>
                                            <c:if test="${u.id != sessionScope.user.id}">
                                                <form action="${pageContext.request.contextPath}/users/delete"
                                                      method="post" class="d-inline"
                                                      onsubmit="return confirm('Supprimer cet utilisateur ?');">
                                                    <input type="hidden" name="id" value="${u.id}"/>
                                                    <button type="submit" class="btn btn-sm btn-outline-danger">Supprimer</button>
                                                </form>
                                            </c:if>
                                            <c:if test="${u.id == sessionScope.user.id}">
                                                <span class="text-muted small">(vous)</span>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="p-4 text-center text-muted">Aucun utilisateur.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
