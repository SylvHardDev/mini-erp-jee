<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
    <title>Produits - Mini ERP</title>

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
            <h1 class="h3 mb-0">Produits</h1>
            <p class="text-muted mb-0">Catalogue et stock</p>
        </div>
        <a href="${pageContext.request.contextPath}/products/new"
           class="btn btn-primary btn-sm">
            Nouveau produit
        </a>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty products}">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0 align-middle">
                            <thead class="table-light">
                            <tr>
                                <th scope="col">Image</th>
                                <th scope="col">#</th>
                                <th scope="col">Référence</th>
                                <th scope="col">Libellé</th>
                                <th scope="col" class="text-end">Prix unitaire</th>
                                <th scope="col" class="text-end">Stock</th>
                                <th scope="col" class="text-end">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <td class="align-middle">
                                        <c:choose>
                                            <c:when test="${not empty product.imageData}">
                                                <img src="${pageContext.request.contextPath}/product-image?id=${product.id}"
                                                     alt="${fn:escapeXml(product.libelle)}"
                                                     class="img-thumbnail"
                                                     style="max-height: 48px; max-width: 64px; object-fit: contain;">
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted small">—</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <th scope="row" class="align-middle">${product.id}</th>
                                    <td class="align-middle">${product.reference}</td>
                                    <td class="align-middle">${product.libelle}</td>
                                    <td class="text-end align-middle"><fmt:formatNumber value="${product.prixUnitaire}" minFractionDigits="2" maxFractionDigits="2"/> €</td>
                                    <td class="text-end align-middle">${product.stock}</td>
                                    <td class="text-end align-middle">
                                        <a class="btn btn-sm btn-outline-secondary me-1"
                                           href="${pageContext.request.contextPath}/products/edit?id=${product.id}">
                                            Modifier
                                        </a>
                                        <form action="${pageContext.request.contextPath}/products/delete"
                                              method="post"
                                              class="d-inline"
                                              onsubmit="return confirm('Supprimer ce produit ?');">
                                            <input type="hidden" name="id" value="${product.id}"/>
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
                        Aucun produit. Cliquez sur « Nouveau produit » pour en ajouter.
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
