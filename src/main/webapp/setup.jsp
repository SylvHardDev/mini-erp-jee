<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Première configuration - Mini ERP</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          crossorigin="anonymous">
</head>
<body class="bg-light d-flex align-items-center justify-content-center" style="min-height: 100vh;">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-12 col-sm-10 col-md-6 col-lg-4">
            <div class="card shadow-sm border-0 rounded-4">
                <div class="card-body p-4">
                    <h1 class="h4 text-center mb-2">Mini ERP</h1>
                    <p class="text-center text-muted mb-4">Aucun utilisateur. Créez le compte administrateur.</p>

                    <form action="${pageContext.request.contextPath}/setup" method="post" novalidate>
                        <div class="mb-3">
                            <label for="username" class="form-label">Identifiant <span class="text-danger">*</span></label>
                            <input type="text"
                                   class="form-control"
                                   id="username"
                                   name="username"
                                   required
                                   autofocus
                                   value="${fn:escapeXml(username)}"
                                   placeholder="ex. admin">
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">Mot de passe <span class="text-danger">*</span></label>
                            <input type="password"
                                   class="form-control"
                                   id="password"
                                   name="password"
                                   required
                                   placeholder="Choisissez un mot de passe">
                        </div>

                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">Confirmer le mot de passe <span class="text-danger">*</span></label>
                            <input type="password"
                                   class="form-control"
                                   id="confirmPassword"
                                   name="confirmPassword"
                                   required
                                   placeholder="Répétez le mot de passe">
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger py-2 mb-3" role="alert">
                                ${error}
                            </div>
                        </c:if>

                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">
                                Créer le compte administrateur
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
</body>
</html>
