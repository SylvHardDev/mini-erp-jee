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
    <title><c:choose><c:when test="${not empty client}">Éditer un client</c:when><c:otherwise>Nouveau client</c:otherwise></c:choose> - Mini ERP</title>

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
    <div class="row justify-content-center">
        <div class="col-12 col-lg-8 col-xl-6">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h1 class="h4 mb-0">
                    <c:choose>
                        <c:when test="${not empty client}">Éditer un client</c:when>
                        <c:otherwise>Nouveau client</c:otherwise>
                    </c:choose>
                </h1>
                <a href="${pageContext.request.contextPath}/clients"
                   class="btn btn-outline-secondary btn-sm">
                    Retour à la liste
                </a>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger py-2 mb-3" role="alert">
                            ${error}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}<c:choose><c:when test='${not empty client}'>/clients/edit</c:when><c:otherwise>/clients/new</c:otherwise></c:choose>"
                          method="post" novalidate>
                        <c:if test="${not empty client}">
                            <input type="hidden" name="id" value="${client.id}"/>
                        </c:if>
                        <div class="mb-3">
                            <label for="name" class="form-label">Nom <span class="text-danger">*</span></label>
                            <input type="text"
                                   class="form-control"
                                   id="name"
                                   name="name"
                                   required
                                   value="<c:choose><c:when test='${not empty client}'>${fn:escapeXml(client.name)}</c:when><c:otherwise>${fn:escapeXml(param.name)}</c:otherwise></c:choose>">
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                            <input type="email"
                                   class="form-control"
                                   id="email"
                                   name="email"
                                   required
                                   value="<c:choose><c:when test='${not empty client}'>${fn:escapeXml(client.email)}</c:when><c:otherwise>${fn:escapeXml(param.email)}</c:otherwise></c:choose>">
                        </div>

                        <div class="mb-3">
                            <label for="phone" class="form-label">Téléphone</label>
                            <input type="text"
                                   class="form-control"
                                   id="phone"
                                   name="phone"
                                   value="<c:choose><c:when test='${not empty client}'>${fn:escapeXml(client.phone)}</c:when><c:otherwise>${fn:escapeXml(param.phone)}</c:otherwise></c:choose>">
                        </div>

                        <div class="mb-3">
                            <label for="address" class="form-label">Adresse</label>
                            <input type="text"
                                   class="form-control"
                                   id="address"
                                   name="address"
                                   value="<c:choose><c:when test='${not empty client}'>${fn:escapeXml(client.address)}</c:when><c:otherwise>${fn:escapeXml(param.address)}</c:otherwise></c:choose>">
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="city" class="form-label">Ville</label>
                                <input type="text"
                                       class="form-control"
                                       id="city"
                                       name="city"
                                       value="<c:choose><c:when test='${not empty client}'>${fn:escapeXml(client.city)}</c:when><c:otherwise>${fn:escapeXml(param.city)}</c:otherwise></c:choose>">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="country" class="form-label">Pays</label>
                                <input type="text"
                                       class="form-control"
                                       id="country"
                                       name="country"
                                       value="<c:choose><c:when test='${not empty client}'>${fn:escapeXml(client.country)}</c:when><c:otherwise>${fn:escapeXml(param.country)}</c:otherwise></c:choose>">
                            </div>
                        </div>

                        <div class="d-flex justify-content-end gap-2 mt-3">
                            <a href="${pageContext.request.contextPath}/clients"
                               class="btn btn-outline-secondary">
                                Annuler
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <c:choose>
                                    <c:when test="${not empty client}">Mettre à jour</c:when>
                                    <c:otherwise>Enregistrer</c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
</body>
</html>

