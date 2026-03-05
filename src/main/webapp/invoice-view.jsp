<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
  if (session == null || session.getAttribute("user") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
  }
  if (request.getAttribute("invoice") == null) {
    response.sendRedirect(request.getContextPath() + "/invoices");
    return;
  }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Facture ${invoice.invoiceNumber} - Mini ERP</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          crossorigin="anonymous">
    <style>
        @media print {
            .no-print { display: none !important; }
            body { padding: 0; }
            .invoice-block { box-shadow: none; border: 1px solid #dee2e6; }
        }
    </style>
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary no-print">
    <div class="container-fluid">
        <a class="navbar-brand fw-semibold" href="${pageContext.request.contextPath}/dashboard.jsp">
            Mini ERP
        </a>
        <div class="d-flex align-items-center gap-2">
            <a class="btn btn-outline-light btn-sm"
               href="${pageContext.request.contextPath}/invoices">
                Liste des factures
            </a>
            <button type="button" class="btn btn-outline-light btn-sm" onclick="window.print();">
                Imprimer / PDF
            </button>
        </div>
    </div>
</nav>

<main class="container py-4">
    <div class="invoice-block card shadow-sm border-0 bg-white p-4 p-md-5">
        <div class="row mb-4">
            <div class="col-6">
                <h1 class="h4 text-primary mb-1">Mini ERP</h1>
                <p class="text-muted small mb-0">Facture</p>
            </div>
            <div class="col-6 text-end">
                <p class="mb-0"><strong>Facture n° ${invoice.invoiceNumber}</strong></p>
                <p class="text-muted small mb-0">Date : ${invoice.invoiceDate}</p>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-6">
                <p class="text-muted small text-uppercase mb-1">Client</p>
                <p class="mb-0 fw-semibold">${invoice.clientName}</p>
                <c:if test="${not empty invoice.clientEmail}">
                    <p class="mb-0 small">${invoice.clientEmail}</p>
                </c:if>
                <c:if test="${not empty invoice.clientAddress}">
                    <p class="mb-0 small">${invoice.clientAddress}</p>
                </c:if>
            </div>
        </div>

        <table class="table table-bordered mb-4">
            <thead class="table-light">
            <tr>
                <th>Référence</th>
                <th>Désignation</th>
                <th class="text-center">Quantité</th>
                <th class="text-end">Prix unitaire</th>
                <th class="text-end">Total</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="line" items="${invoice.lines}">
                <tr>
                    <td>${line.productReference}</td>
                    <td>${line.productLabel}</td>
                    <td class="text-center">${line.quantity}</td>
                    <td class="text-end">${line.unitPrice} €</td>
                    <td class="text-end">${line.lineTotal} €</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div class="row">
            <div class="col-md-6"></div>
            <div class="col-md-6">
                <p class="mb-0 text-end">
                    <strong>Total TTC : ${invoice.totalAmount} €</strong>
                </p>
                <p class="text-muted small text-end mb-0">Statut : ${invoice.status}</p>
                <p class="text-muted small text-end">Commande n° ${invoice.order.id}</p>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
</body>
</html>
