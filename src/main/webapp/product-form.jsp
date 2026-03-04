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
    <title><c:choose><c:when test="${not empty product}">Éditer un produit</c:when><c:otherwise>Nouveau produit</c:otherwise></c:choose> - Mini ERP</title>

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
                        <c:when test="${not empty product}">Éditer un produit</c:when>
                        <c:otherwise>Nouveau produit</c:otherwise>
                    </c:choose>
                </h1>
                <a href="${pageContext.request.contextPath}/products"
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

                    <form action="${pageContext.request.contextPath}<c:choose><c:when test='${not empty product}'>/products/edit</c:when><c:otherwise>/products/new</c:otherwise></c:choose>"
                          method="post" enctype="multipart/form-data" novalidate>
                        <c:if test="${not empty product}">
                            <input type="hidden" name="id" value="${product.id}"/>
                        </c:if>

                        <div class="mb-3">
                            <label for="reference" class="form-label">Référence <span class="text-danger">*</span></label>
                            <input type="text"
                                   class="form-control"
                                   id="reference"
                                   name="reference"
                                   required
                                   placeholder="ex. REF-001"
                                   value="<c:choose><c:when test='${not empty product}'>${fn:escapeXml(product.reference)}</c:when><c:otherwise>${fn:escapeXml(param.reference)}</c:otherwise></c:choose>">
                        </div>

                        <div class="mb-3">
                            <label for="libelle" class="form-label">Libellé <span class="text-danger">*</span></label>
                            <input type="text"
                                   class="form-control"
                                   id="libelle"
                                   name="libelle"
                                   required
                                   placeholder="Nom du produit"
                                   value="<c:choose><c:when test='${not empty product}'>${fn:escapeXml(product.libelle)}</c:when><c:otherwise>${fn:escapeXml(param.libelle)}</c:otherwise></c:choose>">
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="prixUnitaire" class="form-label">Prix unitaire (€) <span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control"
                                       id="prixUnitaire"
                                       name="prixUnitaire"
                                       required
                                       placeholder="0.00"
                                       value="<c:choose><c:when test='${not empty product}'>${product.prixUnitaire}</c:when><c:otherwise>${fn:escapeXml(param.prixUnitaire)}</c:otherwise></c:choose>">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="stock" class="form-label">Stock <span class="text-danger">*</span></label>
                                <input type="number"
                                       class="form-control"
                                       id="stock"
                                       name="stock"
                                       min="0"
                                       required
                                       value="<c:choose><c:when test='${not empty product}'>${product.stock}</c:when><c:otherwise>${fn:escapeXml(param.stock)}</c:otherwise></c:choose>">
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="image" class="form-label">Image</label>
                            <c:if test="${not empty product and not empty product.imageData}">
                                <div class="mb-2">
                                    <img src="${pageContext.request.contextPath}/product-image?id=${product.id}"
                                         alt="Image actuelle"
                                         class="img-thumbnail"
                                         style="max-height: 120px;">
                                    <p class="text-muted small mb-0 mt-1">Image actuelle. Choisir un fichier pour la remplacer.</p>
                                </div>
                            </c:if>
                            <input type="file"
                                   class="form-control"
                                   id="image"
                                   name="image"
                                   accept="image/jpeg,image/png,image/gif,image/webp">
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control"
                                      id="description"
                                      name="description"
                                      rows="2"
                                      placeholder="Optionnel"><c:choose><c:when test='${not empty product}'>${fn:escapeXml(product.description)}</c:when><c:otherwise>${fn:escapeXml(param.description)}</c:otherwise></c:choose></textarea>
                        </div>

                        <div class="d-flex justify-content-end gap-2 mt-3">
                            <a href="${pageContext.request.contextPath}/products"
                               class="btn btn-outline-secondary">
                                Annuler
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <c:choose>
                                    <c:when test="${not empty product}">Mettre à jour</c:when>
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
