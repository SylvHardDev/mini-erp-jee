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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Mini ERP</title>
</head>
<body>
    <h1>Bienvenue, ${sessionScope.user.username}</h1>
    <p>Rôle : ${sessionScope.user.role}</p>
    <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
</body>
</html>
