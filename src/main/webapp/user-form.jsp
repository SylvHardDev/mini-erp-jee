<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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
    <title><c:choose><c:when test="${not empty formUser.id}">Modifier un employé</c:when><c:otherwise>Nouvel employé</c:otherwise></c:choose> - Mini ERP</title>
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
    <div class="row justify-content-center">
        <div class="col-12 col-lg-8 col-xl-6">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h1 class="h4 mb-0">
                    <c:choose><c:when test="${not empty formUser.id}">Modifier un employé</c:when><c:otherwise>Nouvel employé</c:otherwise></c:choose>
                </h1>
                <a href="${pageContext.request.contextPath}/users" class="btn btn-outline-secondary btn-sm">Retour à la liste</a>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger py-2 mb-3" role="alert">${error}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}<c:choose><c:when test='${not empty formUser.id}'>/users/edit</c:when><c:otherwise>/users/new</c:otherwise></c:choose>" method="post">
                        <c:if test="${not empty formUser.id}">
                            <input type="hidden" name="id" value="${formUser.id}"/>
                        </c:if>

                        <div class="mb-3">
                            <label for="username" class="form-label">Identifiant <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="username" name="username" required
                                   value="<c:choose><c:when test='${not empty formUser.username}'>${fn:escapeXml(formUser.username)}</c:when><c:otherwise>${fn:escapeXml(param.username)}</c:otherwise></c:choose>"
                                   <c:if test="${not empty formUser.id}">readonly</c:if>>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">
                                Mot de passe <c:if test="${not empty formUser.id}"><span class="text-muted">(laisser vide pour ne pas modifier)</span></c:if>
                                <c:if test="${empty formUser.id}"><span class="text-danger">*</span></c:if>
                            </label>
                            <input type="password" class="form-control" id="password" name="password"
                                   <c:if test="${empty formUser.id}">required</c:if>
                                   placeholder="<c:choose><c:when test='${not empty formUser.id}'>Nouveau mot de passe (optionnel)</c:when><c:otherwise>Mot de passe</c:otherwise></c:choose>">
                        </div>

                        <div class="mb-3">
                            <label for="role" class="form-label">Rôle <span class="text-danger">*</span></label>
                            <select class="form-select" id="role" name="role" required>
                                <option value="">Choisir...</option>
                                <option value="ADMIN" <c:if test="${formUser.role == 'ADMIN' || param.role == 'ADMIN'}">selected</c:if>>Administrateur</option>
                                <option value="STOCK" <c:if test="${formUser.role == 'STOCK' || param.role == 'STOCK'}">selected</c:if>>Gestionnaire stock</option>
                                <option value="COMMERCIAL" <c:if test="${formUser.role == 'COMMERCIAL' || param.role == 'COMMERCIAL'}">selected</c:if>>Commercial</option>
                            </select>
                        </div>

                        <div class="d-flex justify-content-end gap-2 mt-3">
                            <a href="${pageContext.request.contextPath}/users" class="btn btn-outline-secondary">Annuler</a>
                            <button type="submit" class="btn btn-primary">
                                <c:choose><c:when test="${not empty formUser.id}">Mettre à jour</c:when><c:otherwise>Créer</c:otherwise></c:choose>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
