<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Mini ERP</title>
</head>
<body>
    <h1>Mini ERP</h1>
    <h2>Connexion</h2>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="username">Identifiant</label><br>
        <input type="text" id="username" name="username" required autofocus><br><br>

        <label for="password">Mot de passe</label><br>
        <input type="password" id="password" name="password" required><br><br>

        <button type="submit">Se connecter</button>
    </form>

    <p style="color: red;">${error}</p>
</body>
</html>
