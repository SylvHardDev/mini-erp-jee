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
    <title>Nouvelle commande - Mini ERP</title>

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
        <h1 class="h4 mb-0">Nouvelle commande</h1>
        <a href="${pageContext.request.contextPath}/orders"
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

            <form action="${pageContext.request.contextPath}/orders/new" method="post" id="orderForm">
                <div class="mb-4">
                    <label for="clientId" class="form-label">Client <span class="text-danger">*</span></label>
                    <select class="form-select" id="clientId" name="clientId" required>
                        <option value="">-- Choisir un client --</option>
                        <c:forEach var="c" items="${clients}">
                            <option value="${c.id}">${c.name} – ${c.email}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-2">
                    <label class="form-label">Lignes de commande</label>
                </div>
                <div class="table-responsive mb-3">
                    <table class="table table-bordered" id="linesTable">
                        <thead class="table-light">
                        <tr>
                            <th>Produit</th>
                            <th style="width: 120px;">Quantité</th>
                            <th style="width: 80px;"></th>
                        </tr>
                        </thead>
                        <tbody id="linesBody">
                        <tr class="line-row">
                            <td>
                                <select class="form-select form-select-sm" name="productId">
                                    <option value="">-- Choisir un produit --</option>
                                    <c:forEach var="p" items="${products}">
                                        <option value="${p.id}">${p.reference} – ${p.libelle} (${p.prixUnitaire} €)</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <input type="number" class="form-control form-control-sm" name="quantity" min="1" value="1">
                            </td>
                            <td>
                                <button type="button" class="btn btn-outline-danger btn-sm remove-line" title="Supprimer la ligne">×</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <button type="button" class="btn btn-outline-primary btn-sm mb-3" id="addLine">
                    + Ajouter une ligne
                </button>

                <div class="d-flex justify-content-end gap-2 mt-3">
                    <a href="${pageContext.request.contextPath}/orders"
                       class="btn btn-outline-secondary">
                        Annuler
                    </a>
                    <button type="submit" class="btn btn-primary">
                        Enregistrer la commande
                    </button>
                </div>
            </form>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
<script>
(function() {
    const tbody = document.getElementById('linesBody');
    const addBtn = document.getElementById('addLine');

    addBtn.addEventListener('click', function() {
        const firstRow = tbody.querySelector('tr');
        const newRow = firstRow.cloneNode(true);
        newRow.querySelector('select').selectedIndex = 0;
        newRow.querySelector('input[name="quantity"]').value = '1';
        tbody.appendChild(newRow);
    });

    tbody.addEventListener('click', function(e) {
        if (e.target.classList.contains('remove-line')) {
            const row = e.target.closest('tr');
            if (tbody.querySelectorAll('tr').length > 1) row.remove();
        }
    });
})();
</script>
</body>
</html>
